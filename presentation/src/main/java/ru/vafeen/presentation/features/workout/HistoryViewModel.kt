package ru.vafeen.presentation.features.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.domain.models.Workout
import ru.vafeen.domain.repository.WorkoutRepository
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

/**
 * ViewModel для экрана истории тренировок.
 *
 * @property workoutRepository Репозиторий для доступа к данным о тренировках.
 * @param settingsManager Менеджер настроек приложения.
 */
@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    settingsManager: SettingsManager,
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.insert(
                Workout(
                    dateTime = LocalDateTime.now(),
                    duration = LocalTime.of(0, 2, 5),
                    countOfExercises = 5
                )
            )
        }
    }

    /**
     * Состояние экрана истории, объединяющее список тренировок и настройки.
     */
    val state = combine(
        workoutRepository.getAll(),
        settingsManager.settingsFlow
    ) { workouts, settings ->
        HistoryState(
            workout = workouts,
            settings = settings
        )
    }

    /**
     * Обрабатывает намерения пользователя.
     *
     * @param intent Намерение пользователя для выполнения.
     */
    fun handleIntent(intent: HistoryIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is HistoryIntent.DeleteWorkout -> deleteWorkout(intent.workout)
            }
        }
    }

    /**
     * Удаляет тренировку.
     *
     * @param workout Тренировка для удаления.
     */
    private suspend fun deleteWorkout(workout: Workout) {
        workoutRepository.delete(workout)
    }

}