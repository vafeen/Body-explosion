package ru.vafeen.domain.local_database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.models.Training

/**
 * Репозиторий для работы с упражнениями в локальной базе данных.
 */
interface TrainingLocalRepository {
    /**
     * Возвращает поток со списком всех упражнений.
     */
    fun getAllTrainings(): Flow<List<Training>>

    /**
     * Вставляет список упражнений в базу данных.
     */
    suspend fun insert(trainings: List<Training>)
}
