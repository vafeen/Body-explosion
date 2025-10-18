package ru.vafeen.presentation.features.settings

data class SettingsState(
    val exerciseDurationSeconds: Int? = null,
    val breakDurationSeconds: Int? = null,
    val appVersion: Pair<Int?, String?>? = null
)
