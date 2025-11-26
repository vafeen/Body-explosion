package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vafeen.data.local_database.converters.Converters
import ru.vafeen.data.local_database.dao.TrainingDao
import ru.vafeen.data.local_database.dao.UserDataDao
import ru.vafeen.data.local_database.dao.WorkoutDao
import ru.vafeen.data.local_database.entity.ExerciseEntity
import ru.vafeen.data.local_database.entity.UserDataEntity
import ru.vafeen.data.local_database.entity.WorkoutEntity

/**
 * Основной класс базы данных приложения.
 *
 * Этот класс определяет структуру базы данных, включая таблицы (entities),
 * версионность и предоставляет доступ к Data Access Objects (DAOs).
 *
 * @property trainingDao DAO для доступа к данным упражнений.
 * @property userDataDao DAO для доступа к данным пользователя.
 * @property workoutDao DAO для доступа к данным тренировок.
 */
@Database(
    version = 1,
    entities = [ExerciseEntity::class, UserDataEntity::class, WorkoutEntity::class],
    exportSchema = true,
)
@TypeConverters(value = [Converters::class])
internal abstract class AppDatabase : RoomDatabase() {
    /**
     * Предоставляет DAO для работы с [ExerciseEntity].
     * @return [TrainingDao]
     */
    abstract fun trainingDao(): TrainingDao

    /**
     * Предоставляет DAO для работы с [UserDataEntity].
     * @return [UserDataDao]
     */
    abstract fun userDataDao(): UserDataDao

    /**
     * Предоставляет DAO для работы с [WorkoutEntity].
     * @return [WorkoutDao]
     */
    abstract fun workoutDao(): WorkoutDao

    companion object {
        /**
         * Имя базы данных.
         */
        const val NAME = "AppDatabase"
    }
}
