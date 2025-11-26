package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Представляет запись о тренировке в базе данных.
 *
 * @property dateTime Дата и время начала тренировки. Используется как первичный ключ.
 * @property duration Продолжительность тренировки.
 * @property countOfExercises Количество упражнений в тренировке.
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val dateTime: LocalDateTime,
    val duration: LocalTime,
    val countOfExercises: Int,
)
