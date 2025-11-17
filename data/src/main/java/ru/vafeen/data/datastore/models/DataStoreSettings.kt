package ru.vafeen.data.datastore.models

import kotlinx.serialization.Serializable


@Serializable
data class DataStoreSettings(
    val defaultExerciseDurationMillis: Long = 60000,
    val defaultBreakDurationMillis: Long = 10000,
    val exerciseDurationMillis: Long = 60000,
    val breakDurationMillis: Long = 15000,
)
