package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Exercise
import java.time.LocalTime

/**
 * Состояние экрана настроек.
 *
 * @property exerciseDuration Длительность упражнения.
 * @property tempExerciseDuration Временная длительность упражнения, используемая в диалоге.
 * @property breakDuration Длительность перерыва.
 * @property tempBreakDuration Временная длительность перерыва, используемая в диалоге.
 * @property appVersion Версия приложения (код и имя).
 * @property exercises Список упражнений.
 * @property isExerciseDurationDialogShowed Флаг, показывающий, отображается ли диалог выбора длительности упражнения.
 * @property isBreakDurationDialogShowed Флаг, показывающий, отображается ли диалог выбора длительности перерыва.
 */
data class SettingsState(
    val exerciseDuration: LocalTime? = null,
    val tempExerciseDuration: LocalTime? = null,
    val breakDuration: LocalTime? = null,
    val tempBreakDuration: LocalTime? = null,
    val appVersion: Pair<Int?, String?>,
    val exercises: List<Exercise> = listOf(),
    val isExerciseDurationDialogShowed: Boolean = false,
    val isBreakDurationDialogShowed: Boolean = false,
)
