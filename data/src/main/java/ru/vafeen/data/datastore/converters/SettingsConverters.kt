package ru.vafeen.data.datastore.converters

import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.domain.models.Settings

/**
 * Преобразует [DataStoreSettings] в [Settings].
 */
internal fun DataStoreSettings.toSettings(): Settings = Settings(
    exerciseDuration = exerciseDurationMillis.toLocalTime(),
    breakDuration = breakDurationMillis.toLocalTime(),
    defaultExerciseDuration = defaultExerciseDurationMillis.toLocalTime(),
    defaultBreakDuration = defaultBreakDurationMillis.toLocalTime(),
)

/**
 * Преобразует [Settings] в [DataStoreSettings].
 */
internal fun Settings.toDataStoreSettings(): DataStoreSettings = DataStoreSettings(
    exerciseDurationMillis = exerciseDuration.toMillisOfDay(),
    breakDurationMillis = breakDuration.toMillisOfDay(),
    defaultExerciseDurationMillis = defaultExerciseDuration.toMillisOfDay(),
    defaultBreakDurationMillis = defaultBreakDuration.toMillisOfDay(),
)
