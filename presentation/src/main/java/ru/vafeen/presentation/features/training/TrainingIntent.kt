package ru.vafeen.presentation.features.training

import ru.vafeen.domain.models.Exercise

/**
 * Представляет намерения пользователя на экране тренировки.
 */
internal sealed interface TrainingIntent {
    /**
     * Начать или возобновить тренировку.
     */
    data object StartTraining : TrainingIntent

    /**
     * Приостановить тренировку.
     */
    data object PauseTraining : TrainingIntent

    /**
     * Остановить тренировку.
     */
    data object StopTraining : TrainingIntent

    /**
     * Показать всплывающее сообщение.
     *
     * @property message Сообщение для отображения.
     * @property duration Длительность отображения сообщения (например, Toast.LENGTH_SHORT).
     */
    data class ShowToast(val message: String, val duration: Int) : TrainingIntent
    data class UpdateExercise(val exercise: Exercise) : TrainingIntent
}
