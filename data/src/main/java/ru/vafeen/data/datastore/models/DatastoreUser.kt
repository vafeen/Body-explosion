package ru.vafeen.data.datastore.models

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUser(
    val name: String,
    val username: String,
)