package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.ExerciseEntity

/**
 * Data Access Object (DAO) для [ExerciseEntity].
 * Предоставляет методы для взаимодействия с таблицей `training` в базе данных.
 */
@Dao
internal interface TrainingDao : DataAccessObject<ExerciseEntity>,
    FlowGetAllImplementation<ExerciseEntity> {

    /**
     * Получает все записи из таблицы 'training' в виде потока.
     *
     * @return [Flow] со списком [ExerciseEntity].
     */
    @Query("SELECT * FROM training")
    override fun getAll(): Flow<List<ExerciseEntity>>

    /**
     * Удаляет все записи из таблицы 'training'.
     * Эта операция сбрасывает все счетчики автоинкремента.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM training")
    override suspend fun clear()

}
