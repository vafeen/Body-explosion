package ru.vafeen.data.local_database.converters

import androidx.room.TypeConverter
import ru.vafeen.data.local_database.entity.ExerciseEntity
import ru.vafeen.data.local_database.entity.WorkoutEntity
import ru.vafeen.domain.models.Exercise
import ru.vafeen.domain.models.Workout
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * Преобразует объект базы данных [ExerciseEntity] в доменную модель [Exercise].
 *
 * @return Доменная модель [Exercise].
 */
internal fun ExerciseEntity.toExercise(): Exercise =
    Exercise(
        id = id,
        name = name,
        isIncludedToTraining = isIncludedToTraining,
        duration = Converters().convertToTime(duration)
    )

/**
 * Преобразует доменную модель [Exercise] в объект базы данных [ExerciseEntity].
 *
 * @return Объект базы данных [ExerciseEntity].
 */
internal fun Exercise.toExerciseEntity(): ExerciseEntity =
    ExerciseEntity(
        id = id,
        name = name,
        isIncludedToTraining = isIncludedToTraining,
        duration = Converters().convertFromTime(duration)
    )

/**
 * Преобразует объект базы данных [WorkoutEntity] в доменную модель [Workout].
 *
 * @return Доменная модель [Workout].
 */
internal fun WorkoutEntity.toWorkout(): Workout =
    Workout(
        dateTime = dateTime,
        duration = duration,
        countOfExercises = countOfExercises,
        text = ""
    )

/**
 * Преобразует доменную модель [Workout] в объект базы данных [WorkoutEntity].
 *
 * @return Объект базы данных [WorkoutEntity].
 */
internal fun Workout.toWorkoutEntity(): WorkoutEntity =
    WorkoutEntity(dateTime = dateTime, duration = duration, countOfExercises = countOfExercises)

/**
 * Класс-конвертер для базы данных Room.
 * Предоставляет методы для преобразования сложных типов данных в примитивы, которые Room может сохранять.
 */
class Converters {
    /**
     * Конвертирует [LocalTime] в [Long] для сохранения в базе данных.
     *
     * @param localTime Время для конвертации.
     * @return [Long], представляющий количество секунд от начала дня.
     */
    @TypeConverter
    fun convertFromTime(localTime: LocalTime): Long {
        return localTime.toSecondOfDay().toLong()
    }

    /**
     * Конвертирует [Long] обратно в [LocalTime].
     *
     * @param long Количество секунд от начала дня.
     * @return [LocalTime].
     */
    @TypeConverter
    fun convertToTime(long: Long): LocalTime {
        return LocalTime.ofSecondOfDay(long)
    }

    /**
     * Конвертирует [LocalDateTime] в [Long] для сохранения в базе данных.
     *
     * @param localDateTime Дата и время для конвертации.
     * @return [Long], представляющий количество миллисекунд с начала эпохи.
     */
    @TypeConverter
    fun convertFromDateTime(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    /**
     * Конвертирует [Long] обратно в [LocalDateTime].
     *
     * @param long Количество миллисекунд с начала эпохи.
     * @return [LocalDateTime].
     */
    @TypeConverter
    fun convertToDateTime(long: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(long), ZoneId.systemDefault())
    }
}
