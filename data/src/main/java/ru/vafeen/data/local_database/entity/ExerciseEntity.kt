package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Представляет упражнение в базе данных.
 *
 * @property id Уникальный идентификатор упражнения.
 * @property name Название упражнения.
 * @property isIncludedToTraining Включено ли упражнение в тренировку.
 */
@Entity(tableName = "training")
internal data class ExerciseEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isIncludedToTraining: Boolean,
)
