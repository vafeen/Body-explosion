package ru.vafeen.data.local_database

/**
 * Преобразует один объект в список, содержащий только этот объект.
 *
 * @return Список, содержащий только этот объект.
 */
internal fun <T> T.asList(): List<T> = listOf(this)
