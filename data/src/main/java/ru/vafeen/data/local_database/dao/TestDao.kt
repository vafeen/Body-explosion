package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.TestEntity

@Dao
internal interface TestDao : DataAccessObject<TestEntity>,
    FlowGetAllImplementation<TestEntity> {


    @Query("SELECT * FROM tests")
    override fun getAll(): Flow<List<TestEntity>>

    /**
     * Deletes all records from the characters table.
     * This operation resets all auto-increment counters.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM tests")
    override suspend fun clear()

}
