package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Training

/**
 * Состояние экрана настроек.
 *
 * @property exerciseDurationSeconds Длительность упражнения в секундах.
 * @property breakDurationSeconds Длительность перерыва в секундах.
 * @property appVersion Версия приложения (код и имя).
 * @property trainings Список упражнений.
 */
data class SettingsState(
    val exerciseDurationSeconds: Int? = null,
    val breakDurationSeconds: Int? = null,
    val appVersion: Pair<Int?, String?>? = null,
    val trainings: List<Training> = listOf()
)
