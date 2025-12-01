package ru.vafeen.data.datastore.converters

import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.data.datastore.models.DatastoreUser
import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.User

/**
 * Преобразует модель настроек [DataStoreSettings], используемую на уровне данных,
 * в модель домена [Settings].
 *
 * @return Объект [Settings], представляющий настройки в доменном слое.
 */
internal fun DataStoreSettings.toSettings(): Settings = Settings(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = datastoreUser?.toUser()
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
    datastoreUser = user?.toDatastoreUser()
)

private fun User.toDatastoreUser(): DatastoreUser = DatastoreUser(name, username)
private fun DatastoreUser.toUser(): User = User(name, username)
