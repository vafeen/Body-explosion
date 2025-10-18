package ru.vafeen.presentation.features.settings

data class SettingsState(
    val exerciseDurationMillis: Long? = null,
    val breakDurationMillis: Long? = null,
    val appVersion: Pair<Int?, String?>? = null
)
