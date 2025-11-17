package ru.vafeen.data.datastore.converters

import java.time.LocalTime

/**
 * Конвертер функций расширения для преобразования между [LocalTime] и [Long].
 */

/**
 * Преобразует [LocalTime] в значение типа [Long], представляющее миллисекунды с начала суток.
 *
 * @receiver Время типа [LocalTime] для преобразования.
 * @return Количество миллисекунд с начала суток.
 */
fun LocalTime.toMillisOfDay(): Long = this.toNanoOfDay() / 1_000_000

/**
 * Преобразует значение типа [Long], представляющее миллисекунды с начала суток,
 * в объект [LocalTime].
 *
 * @receiver Количество миллисекунд с начала суток.
 * @return Время типа [LocalTime].
 */
fun Long.toLocalTime(): LocalTime = LocalTime.ofNanoOfDay(this * 1_000_000)
