package ru.vafeen.presentation.features.training

/**
 * Представляет состояния экрана тренировки.
 */
internal sealed interface TrainingState {
    /**
     * Тренировка еще не началась.
     */
    data object NotStarted : TrainingState

    /**
     * Тренировка в процессе выполнения.
     *
     * @property secondsLeft Оставшееся время выполнения упражнения.
     * @property secondsOnOneExercise Общее время на одно упражнение.
     * @property currentExercise Индекс текущего упражнения.
     * @property totalExercises Общее количество упражнений в тренировке.
     */
    data class InProgress(
        val secondsLeft: Int,
        val secondsOnOneExercise: Int,
        val currentExercise: Int,
        val totalExercises: Int,
    ) : TrainingState

    /**
     * Перерыв между упражнениями.
     *
     * @property secondsLeft Оставшееся время отдыха.
     * @property secondsForBreak Общее время на отдых.
     * @property currentExercise Индекс следующего упражнения.
     * @property totalExercises Общее количество упражнений в тренировке.
     */
    data class Break(
        val secondsLeft: Int,
        val secondsForBreak: Int,
        val currentExercise: Int,
        val totalExercises: Int,
    ) : TrainingState

    /**
     * Тренировка приостановлена.
     *
     * @property secondsLeft Оставшееся время выполнения упражнения.
     * @property currentExercise Индекс текущего упражнения.
     * @property totalExercises Общее количество упражнений в тренировке.
     */
    data class PausedTraining(
        val secondsLeft: Int,
        val currentExercise: Int,
        val totalExercises: Int,
    ) : TrainingState

    /**
     * Перерыв приостановлен.
     *
     * @property secondsLeft Оставшееся время отдыха.
     * @property currentExercise Индекс следующего упражнения.
     * @property totalExercises Общее количество упражнений в тренировке.
     */
    data class PausedBreak(
        val secondsLeft: Int,
        val currentExercise: Int,
        val totalExercises: Int,
    ) : TrainingState
}
