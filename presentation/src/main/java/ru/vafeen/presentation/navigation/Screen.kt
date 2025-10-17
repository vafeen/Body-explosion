package ru.vafeen.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


internal sealed interface Screen : NavKey {
    @Serializable
    data object Training : Screen

    @Serializable
    data object Settings : Screen
}