package ru.vafeen.presentation.root

import ru.vafeen.presentation.navigation.Screen

internal sealed interface NavRootEffect {
    data class NavigateTo(val screen: Screen) : NavRootEffect
    data object NavigateBack : NavRootEffect
}