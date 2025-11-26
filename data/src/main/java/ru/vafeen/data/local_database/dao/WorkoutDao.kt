package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.WorkoutEntity

/**
 * Data Access Object (DAO) для [WorkoutEntity].
 * Предоставляет методы для взаимодействия с таблицей `workouts` в базе данных.
 */
@Dao
internal interface WorkoutDao : DataAccessObject<WorkoutEntity>,
    FlowGetAllImplementation<WorkoutEntity> {
    /**
     * Получает все записи из таблицы 'workouts' в виде потока.
     *
     * @return [Flow] со списком [WorkoutEntity].
     */
    @Query("SELECT * FROM workouts")
    override fun getAll(): Flow<List<WorkoutEntity>>

    /**
     * Удаляет все записи из таблицы 'workouts'.
     * Эта операция сбрасывает все счетчики автоинкремента.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM workouts")
    override suspend fun clear()
}
