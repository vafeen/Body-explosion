package ru.vafeen.domain.models

import java.time.LocalTime


data class Settings(
    val id: Long? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val defaultExerciseDuration: LocalTime,
    val defaultBreakDuration: LocalTime,
    val exerciseDuration: LocalTime,
    val breakDuration: LocalTime,
)

