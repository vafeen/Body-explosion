package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.TestEntity

/**
 * Объект доступа к данным (DAO) для [TestEntity].
 */
@Dao
internal interface TestDao : DataAccessObject<TestEntity>,
    FlowGetAllImplementation<TestEntity> {

    /**
     * Получает все записи из таблицы 'tests' в виде потока.
     *
     * @return [Flow] со списком [TestEntity].
     */
    @Query("SELECT * FROM tests")
    override fun getAll(): Flow<List<TestEntity>>

    /**
     * Удаляет все записи из таблицы 'tests'.
     * Эта операция сбрасывает все счетчики автоинкремента.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM tests")
    override suspend fun clear()

}
