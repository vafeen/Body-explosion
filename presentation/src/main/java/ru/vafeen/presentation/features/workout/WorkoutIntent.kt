package ru.vafeen.presentation.features.workout

internal sealed interface WorkoutIntent {
    data object NavigateToHistory : WorkoutIntent
    data object NavigateToSettings : WorkoutIntent

}