package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.entity.UserDataEntity

/**
 * Data Access Object (DAO) для [UserDataEntity].
 * Предоставляет методы для взаимодействия с таблицей `users` в базе данных.
 */
@Dao
internal interface UserDataDao : DataAccessObject<UserDataEntity> {
    /**
     * Получает все записи из таблицы `users`.
     *
     * @return Список всех [UserDataEntity].
     */
    @Query("select * from users")
    fun getAll(): List<UserDataEntity>

    /**
     * Получает пользователя по его имени.
     *
     * @param username Имя пользователя для поиска.
     * @return [UserDataEntity], если найден, иначе null.
     */
    @Query("select * from users where username=:username limit 1")
    fun getByUsername(username: String): UserDataEntity?

    /**
     * Удаляет все записи из таблицы 'users'.
     * Эта операция сбрасывает все счетчики автоинкремента.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM users")
    override suspend fun clear()
}
