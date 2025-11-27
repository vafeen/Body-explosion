package ru.vafeen.presentation.features.training

import androidx.compose.ui.graphics.Color
import ru.vafeen.domain.models.Exercise
import ru.vafeen.presentation.ui.theme.Colors

/**
 * Представляет состояния экрана тренировки.
 */
internal sealed interface TrainingState {
    /**
     * Тренировка еще не началась.
     */
    data class NotStarted(
        val exercises: List<Exercise>,
    ) : TrainingState

    /**
     * Тренировка в процессе выполнения.
     *
     * @property secondsLeft Оставшееся время выполнения упражнения.
     * @property secondsOnOneExercise Общее время на одно упражнение.
     * @property currentExercise Индекс текущего упражнения.
     * @property exercises Список упражнений в тренировке.
     * @property color Цвет для отображения состояния выполнения упражнения.
     */
    data class InProgress(
        val secondsLeft: Int,
        val secondsOnOneExercise: Int,
        val currentExercise: Int,
        val exercises: List<Exercise>,
        val color: Color = Colors.exerciseColor
    ) : TrainingState

    /**
     * Перерыв между упражнениями.
     *
     * @property secondsLeft Оставшееся время отдыха.
     * @property secondsForBreak Общее время на отдых.
     * @property nextExercise Индекс следующего упражнения.
     * @property exercises Список упражнений в тренировке.
     * @property color Цвет для отображения состояния перерыва.
     */
    data class Break(
        val secondsLeft: Int,
        val secondsForBreak: Int,
        val nextExercise: Int,
        val exercises: List<Exercise>,
        val color: Color = Colors.breakColor
    ) : TrainingState

    /**
     * Тренировка приостановлена.
     *
     * @property secondsLeft Оставшееся время выполнения упражнения.
     * @property currentExercise Индекс текущего упражнения.
     * @property exercises Список упражнений в тренировке.
     * @property color Цвет для отображения состояния приостановленной тренировки.
     */
    data class PausedTraining(
        val secondsLeft: Int,
        val currentExercise: Int,
        val exercises: List<Exercise>,
        val color: Color = Colors.breakColor
    ) : TrainingState

    /**
     * Перерыв приостановлен.
     *
     * @property secondsLeft Оставшееся время отдыха.
     * @property nextExercise Индекс следующего упражнения.
     * @property exercises Список упражнений в тренировке.
     * @property color Цвет для отображения состояния приостановленного перерыва.
     */
    data class PausedBreak(
        val secondsLeft: Int,
        val nextExercise: Int,
        val exercises: List<Exercise>,
        val color: Color = Colors.breakColor
    ) : TrainingState
}
