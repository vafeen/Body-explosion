package ru.vafeen.data.local_database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.converters.toTraining
import ru.vafeen.data.local_database.converters.toTrainingEntity
import ru.vafeen.domain.local_database.TrainingLocalRepository
import ru.vafeen.domain.models.Training
import javax.inject.Inject

/**
 * Реализация [TrainingLocalRepository] с использованием Room.
 *
 * @property db Экземпляр базы данных [AppDatabase].
 */
internal class RoomTrainingLocalRepository @Inject constructor(
    private val db: AppDatabase
) : TrainingLocalRepository {
    private val trainingDao = db.trainingDao()

    /**
     * {@inheritDoc}
     */
    override fun getAllTrainings(): Flow<List<Training>> =
        trainingDao.getAll().map { list ->
            list.map {
                it.toTraining()
            }.sortedBy { it.id }
        }

    /**
     * {@inheritDoc}
     */
    override suspend fun insert(trainings: List<Training>) =
        trainingDao.insert(entities = trainings.map { it.toTrainingEntity() })

}
