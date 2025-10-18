package ru.vafeen.domain.models

interface SettingsModel {
    val exerciseDurationMillis: Long
    val breakDurationMillis: Long
}

data class Settings(
    override val exerciseDurationMillis: Long,
    override val breakDurationMillis: Long,
) : SettingsModel

