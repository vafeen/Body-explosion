package ru.vafeen.presentation.common.utils

import kotlin.math.roundToInt

internal fun Long.bytesToMBytes(): Long = this / (1024 * 1024)

internal fun Long.roundToOneDecimal(): Float = this.let { ((it * 10f).roundToInt() / 10f) }

