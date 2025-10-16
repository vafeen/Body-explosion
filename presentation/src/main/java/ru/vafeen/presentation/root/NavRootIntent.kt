package ru.vafeen.presentation.root

internal sealed interface NavRootIntent {
    data object CheckUpdates : NavRootIntent
    data object UpdateApp : NavRootIntent
}