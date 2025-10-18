package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.Training

internal sealed interface SettingsIntent {
    data class UpdateSettings(val updating: (Settings) -> Settings) : SettingsIntent
    data object ResetDuration : SettingsIntent
    data class UpdateTraining(val training: Training) : SettingsIntent
}