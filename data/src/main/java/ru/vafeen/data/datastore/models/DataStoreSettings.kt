package ru.vafeen.data.datastore.models

import kotlinx.serialization.Serializable


@Serializable
data class DataStoreSettings(
    val defaultExerciseDurationMillis: Int = 60000,
    val defaultBreakDurationMillis: Int = 10000,
    val exerciseDurationMillis: Int = 60000,
    val breakDurationMillis: Int = 15000,
)
