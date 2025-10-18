package ru.vafeen.data.datastore.converters

import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.domain.models.Settings

/**
 * Преобразует [DataStoreSettings] в [Settings].
 */
internal fun DataStoreSettings.toSettings(): Settings = Settings(
    exerciseDurationMillis = exerciseDurationMillis,
    breakDurationMillis = breakDurationMillis
)

/**
 * Преобразует [Settings] в [DataStoreSettings].
 */
internal fun Settings.toDataStoreSettings(): DataStoreSettings = DataStoreSettings(
    exerciseDurationMillis = exerciseDurationMillis,
    breakDurationMillis = breakDurationMillis
)
