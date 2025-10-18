package ru.vafeen.presentation.features.training

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.root.NavRootIntent
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


private const val TOTAL_EXERCISES = 10

/**
 * ViewModel для экрана тренировки, управляющая состоянием и логикой таймера.
 *
 * @property state Поток текущего состояния экрана тренировки.
 */
@HiltViewModel(assistedFactory = TrainingViewModel.Factory::class)
internal class TrainingViewModel @AssistedInject constructor(
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
    private val settingsManager: SettingsManager,
) : ViewModel() {
    private val settings = settingsManager.settingsFlow.value
    private var SECONDS_FOR_EXERCISE = settings.exerciseDurationSeconds
    private var SECONDS_FOR_BREAK = settings.breakDurationSeconds
    private val _state = MutableStateFlow<TrainingState>(TrainingState.NotStarted)
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<TrainingEffect>()
    val effects = _effects.asSharedFlow()

    private val timerMutex = ReentrantLock()
    private var trainingJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect { settings ->
                SECONDS_FOR_EXERCISE = settings.exerciseDurationSeconds
                SECONDS_FOR_BREAK = settings.breakDurationSeconds
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
            }
        }
    }

    private fun navigateToSettings() =
        sendRootIntent(NavRootIntent.NavigateTo(Screen.Settings))

    private suspend fun showToast(message: String) =
        _effects.emit(TrainingEffect.ShowToast(message, Toast.LENGTH_SHORT))


    /**
     * Запускает или возобновляет тренировку.
     * Если тренировка не была начата, запускает её с первого упражнения.
     * Если была на паузе, возобновляет с сохраненного момента.
     */
    private suspend fun startTraining() {
        val currentState = _state.value
        if (currentState is TrainingState.InProgress) return

        _state.value = when (currentState) {
            is TrainingState.PausedTraining -> TrainingState.InProgress(
                secondsLeft = currentState.secondsLeft,
                secondsOnOneExercise = SECONDS_FOR_EXERCISE,
                currentExercise = currentState.currentExercise,
                totalExercises = TOTAL_EXERCISES
            )

            is TrainingState.PausedBreak -> TrainingState.Break(
                secondsLeft = currentState.secondsLeft,
                secondsForBreak = SECONDS_FOR_BREAK,
                currentExercise = currentState.currentExercise,
                totalExercises = TOTAL_EXERCISES
            )

            else -> TrainingState.InProgress(
                secondsLeft = SECONDS_FOR_EXERCISE,
                secondsOnOneExercise = SECONDS_FOR_EXERCISE,
                currentExercise = 0,
                totalExercises = TOTAL_EXERCISES
            )
        }

        startTimer()
    }

    /**
     * Запускает внутренний таймер, который обновляет состояние каждую секунду.
     * Обрабатывает переходы между состояниями InProgress и Break.
     */
    private suspend fun startTimer() = timerMutex.withLock {
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
                            if (currentState.currentExercise < TOTAL_EXERCISES - 1) {
                                _state.value = TrainingState.Break(
                                    secondsLeft = SECONDS_FOR_BREAK,
                                    secondsForBreak = SECONDS_FOR_BREAK,
                                    currentExercise = currentState.currentExercise,
                                    totalExercises = TOTAL_EXERCISES
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
                            _state.value = TrainingState.InProgress(
                                secondsLeft = SECONDS_FOR_EXERCISE,
                                secondsOnOneExercise = SECONDS_FOR_EXERCISE,
                                currentExercise = currentState.currentExercise + 1,
                                totalExercises = TOTAL_EXERCISES
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private suspend fun stopTimer() = timerMutex.withLock {
        trainingJob?.cancel()
        trainingJob = null
    }

    /**
     * Останавливает тренировку и сбрасывает состояние к начальному.
     */
    private suspend fun stopTraining() {
        stopTimer()
        _state.value = TrainingState.NotStarted
    }

    /**
     * Приостанавливает тренировку, если она в процессе выполнения.
     * Сохраняет текущее состояние в Paused.
     */
    private suspend fun pauseTraining() {
        val currentState = _state.value
        when (currentState) {
            is TrainingState.InProgress -> {
                stopTimer()
                _state.value = TrainingState.PausedTraining(
                    secondsLeft = currentState.secondsLeft,
                    currentExercise = currentState.currentExercise,
                    totalExercises = TOTAL_EXERCISES
                )
            }

            is TrainingState.Break -> {
                stopTimer()
                _state.value = TrainingState.PausedBreak(
                    secondsLeft = currentState.secondsLeft,
                    currentExercise = currentState.currentExercise,
                    totalExercises = TOTAL_EXERCISES
                )
            }

            else -> {}
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): TrainingViewModel
    }
}
