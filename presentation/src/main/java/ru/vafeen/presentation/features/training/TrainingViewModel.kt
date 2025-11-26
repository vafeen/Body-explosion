package ru.vafeen.presentation.features.training

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.domain.models.Exercise
import ru.vafeen.domain.repository.ExerciseRepository
import ru.vafeen.domain.service.MusicPlayer
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.root.NavRootIntent
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * ViewModel для экрана тренировки.
 * Управляет состоянием экрана, логикой таймера и взаимодействием с репозиториями.
 * Жизненный цикл [MusicPlayer] управляется вручную для оптимизации ресурсов:
 * плеер инициализируется при старте тренировки и освобождается при ее завершении.
 *
 * @param context Контекст приложения, используемый для инициализации [MusicPlayer].
 * @param sendRootIntent Функция для отправки навигационных интентов.
 * @param exerciseRepository Репозиторий для доступа к данным о тренировках.
 * @param settingsManager Менеджер для доступа к настройкам приложения.
 * @param musicPlayer Экземпляр плеера для управления воспроизведением музыки.
 */
@HiltViewModel(assistedFactory = TrainingViewModel.Factory::class)
internal class TrainingViewModel @AssistedInject constructor(
    @ApplicationContext context: Context,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
    private val exerciseRepository: ExerciseRepository,
    private val settingsManager: SettingsManager,
    private val musicPlayer: MusicPlayer
) : ViewModel() {
    private val settings = settingsManager.settingsFlow.value
    private var secondsForExercise = settings.exerciseDuration.toSecondOfDay()
    private var secondsForBreak = settings.breakDuration.toSecondOfDay()
    private var exercises = listOf<Exercise>()
    private val _state = MutableStateFlow<TrainingState>(TrainingState.NotStarted)

    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<TrainingEffect>()

    val effects = _effects.asSharedFlow()

    private val timerMutex = ReentrantLock()
    private var trainingJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect { settings ->
                secondsForExercise = settings.exerciseDuration.toSecondOfDay()
                secondsForBreak = settings.breakDuration.toSecondOfDay()
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val defaultExercises = listOf(
                Exercise(1, "Прыжки", true),
                Exercise(2, "Пресс", true),
                Exercise(3, "Выседы", true),
                Exercise(4, "Отжимания", true),
                Exercise(5, "Складка", true),
                Exercise(6, "Планка", true),
                Exercise(7, "Круги", false),
                Exercise(8, "Рукоход", true),
                Exercise(9, "Разгибания", true),
                Exercise(10, "Лесенка", true),
                Exercise(11, "Лодочка", true),
                Exercise(12, "Флажок", false)
            )
            val trainings = exerciseRepository.getAllExercises().first()
            if (trainings.isEmpty()) exerciseRepository.insert(exercises = defaultExercises)
            exerciseRepository.getAllExercises().collect { trainings ->
                exercises = trainings.filter { it.isIncludedToTraining }
            }
        }
    }

    /**
     * Обрабатывает намерения пользователя, запуская, приостанавливая или останавливая тренировку.
     *
     * @param intent Намерение, полученное от UI.
     */
    fun handleIntent(intent: TrainingIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                TrainingIntent.StopTraining -> stopTraining()
                TrainingIntent.StartTraining -> startTraining()
                TrainingIntent.PauseTraining -> pauseTraining()
                is TrainingIntent.ShowToast -> showToast(intent.message)
                TrainingIntent.NavigateToSettings -> navigateToSettings()
                TrainingIntent.NavigateToHistory -> navigateToHistory()
            }
        }
    }

    private fun navigateToHistory() = sendRootIntent(NavRootIntent.NavigateTo(Screen.History))
    private fun navigateToSettings() =
        sendRootIntent(NavRootIntent.NavigateTo(Screen.Settings))

    private suspend fun showToast(message: String) =
        _effects.emit(TrainingEffect.ShowToast(message, Toast.LENGTH_SHORT))

    /**
     * Запускает или возобновляет тренировку. Инициализирует [MusicPlayer],
     * если он не был готов. Управляет состоянием плеера в зависимости от
     * текущего состояния тренировки.
     */
    private fun startTraining() {
        val currentState = _state.value
        if (currentState is TrainingState.InProgress) return
        if (!musicPlayer.isInitialized()) {
            musicPlayer.init()
        }

        _state.value = when (currentState) {
            is TrainingState.PausedTraining -> TrainingState.InProgress(
                secondsLeft = currentState.secondsLeft,
                secondsOnOneExercise = secondsForExercise,
                currentExercise = currentState.currentExercise,
                exercises = exercises
            ).also { musicPlayer.resume() }

            is TrainingState.PausedBreak -> TrainingState.Break(
                secondsLeft = currentState.secondsLeft,
                secondsForBreak = secondsForBreak,
                nextExercise = currentState.nextExercise,
                exercises = exercises
            )

            else -> TrainingState.InProgress(
                secondsLeft = secondsForExercise,
                secondsOnOneExercise = secondsForExercise,
                currentExercise = 0,
                exercises = exercises
            ).also { musicPlayer.restart() }
        }

        startTimer()
    }

    /**
     * Запускает внутренний таймер, который обновляет состояние каждую секунду.
     * Обрабатывает переходы между состояниями InProgress и Break.
     */
    private fun startTimer() = timerMutex.withLock {
        trainingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(1000)
                val currentState = _state.value

                when (currentState) {
                    is TrainingState.InProgress -> {
                        val newSeconds = currentState.secondsLeft - 1
                        if (newSeconds > 0) {
                            _state.value = currentState.copy(secondsLeft = newSeconds)
                        } else {
                            if (currentState.currentExercise < exercises.size - 1) {
                                musicPlayer.pause()
                                _state.value = TrainingState.Break(
                                    secondsLeft = secondsForBreak,
                                    secondsForBreak = secondsForBreak,
                                    nextExercise = currentState.currentExercise + 1,
                                    exercises = exercises
                                )
                            } else {
                                stopTraining()
                            }
                        }
                    }

                    is TrainingState.Break -> {
                        val newSeconds = currentState.secondsLeft - 1
                        if (newSeconds > 0) {
                            _state.value = currentState.copy(secondsLeft = newSeconds)
                        } else {
                            musicPlayer.restart()
                            _state.value = TrainingState.InProgress(
                                secondsLeft = secondsForExercise,
                                secondsOnOneExercise = secondsForExercise,
                                currentExercise = currentState.nextExercise,
                                exercises = exercises
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun stopTimer() = timerMutex.withLock {
        trainingJob?.cancel()
        trainingJob = null
    }

    /**
     * Останавливает тренировку, сбрасывает состояние к начальному и освобождает
     * ресурсы [MusicPlayer].
     */
    private fun stopTraining() {
        stopTimer()
        musicPlayer.pause()
        musicPlayer.release()

        _state.value = TrainingState.NotStarted
    }

    /**
     * Приостанавливает тренировку и воспроизведение музыки.
     */
    private fun pauseTraining() {
        musicPlayer.pause()
        when (val currentState = _state.value) {
            is TrainingState.InProgress -> {
                stopTimer()
                _state.value = TrainingState.PausedTraining(
                    secondsLeft = currentState.secondsLeft,
                    currentExercise = currentState.currentExercise,
                    exercises = exercises
                )
            }

            is TrainingState.Break -> {
                stopTimer()
                _state.value = TrainingState.PausedBreak(
                    secondsLeft = currentState.secondsLeft,
                    nextExercise = currentState.nextExercise,
                    exercises = exercises
                )
            }

            else -> {}
        }
    }

    /**
     * Освобождает ресурсы [MusicPlayer] при уничтожении ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        musicPlayer.release()
    }


    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [TrainingViewModel].
         * @param sendRootIntent Функция для отправки навигационных интентов.
         */
        fun create(sendRootIntent: (NavRootIntent) -> Unit): TrainingViewModel
    }
}
