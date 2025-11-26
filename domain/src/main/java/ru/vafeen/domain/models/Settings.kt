package ru.vafeen.domain.models

import java.time.LocalTime


data class Settings(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val defaultExerciseDuration: LocalTime,
    val defaultBreakDuration: LocalTime,
    val exerciseDuration: LocalTime,
    val breakDuration: LocalTime,
) {
    companion object {
        fun default() = Settings(
            accessToken = null,
            refreshToken = null,
            defaultExerciseDuration = LocalTime.MIN,
            defaultBreakDuration = LocalTime.MIN,
            exerciseDuration = LocalTime.MIN,
            breakDuration = LocalTime.MIN
        )
    }
}

