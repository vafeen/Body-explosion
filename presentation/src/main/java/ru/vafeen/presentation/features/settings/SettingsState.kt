package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Training

data class SettingsState(
    val exerciseDurationSeconds: Int? = null,
    val breakDurationSeconds: Int? = null,
    val appVersion: Pair<Int?, String?>? = null,
    val trainings: List<Training> = listOf()
)
