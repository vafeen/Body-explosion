package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.AppDatabase
import ru.vafeen.data.local_database.converters.toExercise
import ru.vafeen.data.local_database.converters.toExerciseEntity
import ru.vafeen.domain.models.Exercise
import ru.vafeen.domain.repository.ExerciseRepository
import javax.inject.Inject

/**
 * Реализация [ExerciseRepository] для локальной базы данных, использующая Room.
 *
 * @param db Экземпляр базы данных [ru.vafeen.data.local_database.AppDatabase].
 */
internal class RoomExerciseRepository @Inject constructor(
    db: AppDatabase
) : ExerciseRepository {
    private val trainingDao = db.trainingDao()

    /**
     * Получает все упражнения из базы данных в виде потока, отсортированные по ID.
     *
     * @return [Flow], содержащий список [Exercise].
     */
    override fun getAllExercises(): Flow<List<Exercise>> = trainingDao.getAll().map { list ->
        list.map { it.toExercise() }.sortedBy { it.id }
    }

    /**
     * Вставляет список упражнений в базу данных.
     *
     * @param exercises Список [Exercise] для вставки.
     */
    override suspend fun insert(exercises: List<Exercise>) =
        trainingDao.insert(entities = exercises.map { it.toExerciseEntity() })

    /**
     * Удаляет список упражнений из базы данных.
     *
     * @param exercises Список [Exercise] для удаления.
     */
    override suspend fun delete(exercises: List<Exercise>) =
        trainingDao.delete(entities = exercises.map { it.toExerciseEntity() })

}
