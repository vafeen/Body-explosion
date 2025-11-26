package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Представляет данные пользователя в базе данных.
 *
 * @property username Уникальное имя пользователя (используется как первичный ключ).
 * @property passwordHash Хэш пароля пользователя.
 * @property name Имя пользователя.
 */
@Entity(tableName = "users")
internal data class UserDataEntity(
    @PrimaryKey val username: String,
    val passwordHash: String,
    val name: String
)
