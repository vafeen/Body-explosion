package ru.vafeen.backend

/**
 * Внутренние данные пользователя, хранящиеся на сервере.
 *
 * @property id Уникальный идентификатор пользователя.
 * @property username Уникальное имя пользователя (используется как ключ).
 * @property passwordHash Хэш пароля (никогда не хранится в открытом виде).
 * @property name Полное имя пользователя.
 */
internal data class UserData(
    val id: Long,
    val username: String,
    val passwordHash: String,
    val name: String
)