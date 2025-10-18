package ru.vafeen.domain.models


data class Settings(
    val defaultExerciseDurationSeconds: Int,
    val defaultBreakDurationSeconds: Int,
    val exerciseDurationSeconds: Int,
    val breakDurationSeconds: Int,
)

