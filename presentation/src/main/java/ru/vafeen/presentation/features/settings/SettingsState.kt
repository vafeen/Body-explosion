package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Training
import java.time.LocalTime

/**
 * Состояние экрана настроек.
 *
 * @property exerciseDuration Длительность упражнения в секундах.
 * @property breakDuration Длительность перерыва в секундах.
 * @property appVersion Версия приложения (код и имя).
 * @property trainings Список упражнений.
 */
data class SettingsState(
    val exerciseDuration: LocalTime? = null,
    val tempExerciseDuration: LocalTime? = null,
    val breakDuration: LocalTime? = null,
    val tempBreakDuration: LocalTime? = null,
    val appVersion: Pair<Int?, String?>? = null,
    val trainings: List<Training> = listOf(),
    val isExerciseDurationDialogShowed: Boolean = false,
    val isBreakDurationDialogShowed: Boolean = false,
)
