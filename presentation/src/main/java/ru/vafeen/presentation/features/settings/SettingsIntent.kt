package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Settings

internal sealed interface SettingsIntent {
    data class UpdateSettings(val updating: (Settings) -> Settings) : SettingsIntent
    data object ResetDuration : SettingsIntent
}