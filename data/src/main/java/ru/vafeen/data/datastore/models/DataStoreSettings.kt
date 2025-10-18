package ru.vafeen.data.datastore.models

import kotlinx.serialization.Serializable
import ru.vafeen.domain.models.SettingsModel

@Serializable
data class DataStoreSettings(
    override val exerciseDurationMillis: Long = 60000,
    override val breakDurationMillis: Long = 15000,
) : SettingsModel
