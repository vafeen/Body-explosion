package ru.vafeen.data.datastore.converters

import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.domain.models.Settings

/**
 * Преобразует модель настроек [DataStoreSettings], используемую на уровне данных,
 * в модель домена [Settings].
 *
 * @return Объект [Settings], представляющий настройки в доменном слое.
 */
internal fun DataStoreSettings.toSettings(): Settings = Settings(
    accessToken = accessToken,
    refreshToken = refreshToken,
    exerciseDuration = exerciseDurationMillis.toLocalTime(),
    breakDuration = breakDurationMillis.toLocalTime(),
    defaultExerciseDuration = defaultExerciseDurationMillis.toLocalTime(),
    defaultBreakDuration = defaultBreakDurationMillis.toLocalTime(),
)

/**
 * Преобразует доменную модель настроек [Settings]
 * в модель [DataStoreSettings] для сохранения в DataStore.
 *
 * @return Объект [DataStoreSettings] для хранения на уровне данных.
 */
internal fun Settings.toDataStoreSettings(): DataStoreSettings = DataStoreSettings(
    accessToken = accessToken,
    refreshToken = refreshToken,
    exerciseDurationMillis = exerciseDuration.toMillisOfDay(),
    breakDurationMillis = breakDuration.toMillisOfDay(),
    defaultExerciseDurationMillis = defaultExerciseDuration.toMillisOfDay(),
    defaultBreakDurationMillis = defaultBreakDuration.toMillisOfDay(),
)
