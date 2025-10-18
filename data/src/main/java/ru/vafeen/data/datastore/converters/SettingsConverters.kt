package ru.vafeen.data.datastore.converters

import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.domain.models.Settings

/**
 * Преобразует [DataStoreSettings] в [Settings].
 */
internal fun DataStoreSettings.toSettings(): Settings = Settings(
    exerciseDurationSeconds = exerciseDurationMillis.millisToSeconds(),
    breakDurationSeconds = breakDurationMillis.millisToSeconds(),
    defaultExerciseDurationSeconds = defaultExerciseDurationMillis.millisToSeconds(),
    defaultBreakDurationSeconds = defaultBreakDurationMillis.millisToSeconds(),
)

/**
 * Преобразует [Settings] в [DataStoreSettings].
 */
internal fun Settings.toDataStoreSettings(): DataStoreSettings = DataStoreSettings(
    exerciseDurationMillis = exerciseDurationSeconds.secondsToMillis(),
    breakDurationMillis = breakDurationSeconds.secondsToMillis(),
    defaultExerciseDurationMillis = defaultExerciseDurationSeconds.secondsToMillis(),
    defaultBreakDurationMillis = defaultBreakDurationSeconds.secondsToMillis(),
)
