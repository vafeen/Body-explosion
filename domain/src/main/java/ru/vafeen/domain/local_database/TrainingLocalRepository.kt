package ru.vafeen.domain.local_database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.models.Training

/**
 * Репозиторий для работы с локальными данными для тестирования.
 */
interface TrainingLocalRepository {
    fun getAllTrainings(): Flow<List<Training>>
    suspend fun insert(trainings: List<Training>)
}
