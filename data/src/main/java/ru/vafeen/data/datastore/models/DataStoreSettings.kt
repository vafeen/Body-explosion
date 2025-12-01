package ru.vafeen.data.datastore.models

import kotlinx.serialization.Serializable


@Serializable
data class DataStoreSettings(
    val datastoreUser: DatastoreUser? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
)
