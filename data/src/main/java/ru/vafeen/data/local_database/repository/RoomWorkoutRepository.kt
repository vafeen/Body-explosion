package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.AppDatabase
import ru.vafeen.data.local_database.asList
import ru.vafeen.data.local_database.converters.toWorkout
import ru.vafeen.data.local_database.converters.toWorkoutEntity
import ru.vafeen.domain.models.Workout
import ru.vafeen.domain.repository.WorkoutRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация [WorkoutRepository] для локальной базы данных, использующая Room.
 *
 * @param db Экземпляр базы данных [ru.vafeen.data.local_database.AppDatabase].
 */
@Singleton
internal class RoomWorkoutRepository @Inject constructor(
    private val db: AppDatabase
) : WorkoutRepository {
    private val dao = db.workoutDao()

    /**
     * Вставляет тренировку в базу данных.
     *
     * @param workout Тренировка [Workout] для вставки.
     */
    override suspend fun insert(workout: Workout) =
        dao.insert(workout.toWorkoutEntity().asList())

    /**
     * Получает все тренировки из базы данных в виде потока, отсортированные по дате и времени.
     *
     * @return [Flow], содержащий список [Workout].
     */
    override fun getAll(): Flow<List<Workout>> =
        dao.getAll().map { list -> list.map { it.toWorkout() }.sortedBy { it.dateTime } }

    /**
     * Удаляет тренировку из базы данных.
     *
     * @param workout Тренировка [Workout] для удаления.
     */
    override suspend fun delete(workout: Workout) = dao.delete(workout.toWorkoutEntity().asList())
}
