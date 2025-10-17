package ru.vafeen.presentation.root

import ru.vafeen.presentation.navigation.Screen

internal sealed interface NavRootIntent {
    data object CheckUpdates : NavRootIntent
    data object UpdateApp : NavRootIntent
    data class NavigateTo(val screen: Screen) : NavRootIntent
    data object NavigateBack : NavRootIntent
}