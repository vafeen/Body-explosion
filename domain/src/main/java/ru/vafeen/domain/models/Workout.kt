package ru.vafeen.domain.models

import java.time.LocalDateTime
import java.time.LocalTime

data class Workout(
    val dateTime: LocalDateTime,
    val duration: LocalTime,
    val countOfExercises: Int,
)
