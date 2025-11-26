package ru.vafeen.presentation.common.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

internal fun Long.bytesToMBytes(): Long = this / (1024 * 1024)

internal fun Long.roundToOneDecimal(): Float = this.let { ((it * 10f).roundToInt() / 10f) }

internal fun LocalTime.formatAsTime(
    hours: Boolean = true,
    minutes: Boolean = true,
    seconds: Boolean = true,
): String = this.format(
    DateTimeFormatter.ofPattern(
        (if (hours) "HH" else "") +
                (if (hours && (minutes || seconds)) ":" else "") +
                (if (minutes) "mm" else "") +
                (if (seconds) ":" else "") +
                (if (seconds) "ss" else "")
    )
)

internal fun LocalDate.formatAsDate(): String =
    this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

internal fun LocalDateTime.formatAsLocalDateTime(
    hours: Boolean = true,
    minutes: Boolean = true,
    seconds: Boolean = true,
): String =
    "${this.toLocalDate().formatAsDate()} ${
        this.toLocalTime().formatAsTime(hours, minutes, seconds)
    }"

fun Int.getTimeDefaultStr(): String = "${if (this <= 9) "0" else ""}$this"
fun Int.pixelsToDp(context: Context): Float =
    this / (context.resources.displayMetrics.densityDpi / 160f)

/**
 * Функция для генерации случайного цвета.
 * Создает случайный цвет, где каждый канал (красный, зеленый, синий) выбирается случайным образом.
 * Альфа-канал (прозрачность) всегда равен 255 (полностью непрозрачный).
 *
 * @return Случайный цвет.
 */
internal fun generateRandomColor(): Color = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256),
    alpha = 255
)