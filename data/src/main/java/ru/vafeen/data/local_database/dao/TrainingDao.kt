package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.dao.base.DataAccessObject
import ru.vafeen.data.local_database.dao.base.FlowGetAllImplementation
import ru.vafeen.data.local_database.entity.TrainingEntity

/**
 * Объект доступа к данным (DAO) для [TrainingEntity].
 */
@Dao
internal interface TrainingDao : DataAccessObject<TrainingEntity>,
    FlowGetAllImplementation<TrainingEntity> {

    /**
     * Получает все записи из таблицы 'training' в виде потока.
     *
     * @return [Flow] со списком [TrainingEntity].
     */
    @Query("SELECT * FROM training")
    override fun getAll(): Flow<List<TrainingEntity>>

    /**
     * Удаляет все записи из таблицы 'training'.
     * Эта операция сбрасывает все счетчики автоинкремента.
     *
     * @see DataAccessObject.clear
     */
    @Query("DELETE FROM training")
    override suspend fun clear()

}
