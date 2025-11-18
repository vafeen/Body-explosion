package ru.vafeen.presentation.root

import ru.vafeen.domain.models.Release

data class NavRootState(
    val isUpdateNeeded: Boolean = false,
    val release: Release? = null,
    val isUpdateInProcess: Boolean = false,
    val percentage: Float = 0f,
    val version: Pair<Int?, String?>,
)
