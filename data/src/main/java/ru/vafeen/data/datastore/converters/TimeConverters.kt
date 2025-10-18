package ru.vafeen.data.datastore.converters


internal fun Int.millisToSeconds() = this / 1000
internal fun Int.secondsToMillis() = this * 1000