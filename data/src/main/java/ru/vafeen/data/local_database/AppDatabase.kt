package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vafeen.data.local_database.dao.TrainingDao
import ru.vafeen.data.local_database.entity.TrainingEntity

/**
 * Основной класс базы данных приложения.
 *
 * @property trainingDao Предоставляет доступ к [TrainingDao].
 */
@Database(
    version = 1,
    entities = [TrainingEntity::class],
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun trainingDao(): TrainingDao

    companion object {
        const val NAME = "AppDatabase"
    }
}
