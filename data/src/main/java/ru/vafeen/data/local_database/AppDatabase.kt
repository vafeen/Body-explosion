package ru.vafeen.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vafeen.data.local_database.dao.TestDao
import ru.vafeen.data.local_database.entity.TestEntity

/**
 * Основной класс базы данных приложения.
 *
 * @property testDao Предоставляет доступ к [TestDao].
 */
@Database(
    version = 1,
    entities = [TestEntity::class],
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao

    companion object {
        const val NAME = "TestDb"
    }
}
