package ru.vafeen.data.local_database.dao.base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy


/**
 * Родительский интерфейс DAO с базовыми методами
 */
@Dao
internal interface DataAccessObject<T> {

    /**
     * Вставка и обновление в базе данных одной или нескольких сущностей
     * @param entities [Набор сущностей для размещения в базе данных]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<T>)

    /**
     * Удаление из базы данных одной или нескольких сущностей
     * @param entities [Набор сущностей для удаления из базы данных]
     */
    @Delete
    suspend fun delete(entities: List<T>)

    /**
     * Очистка таблицы
     */
    suspend fun clear()
}