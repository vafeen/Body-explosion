package ru.vafeen.presentation.features.history

import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.Workout

data class HistoryState(
    val workout: List<Workout> = listOf(),
    val settings: Settings = Settings.default(),
)