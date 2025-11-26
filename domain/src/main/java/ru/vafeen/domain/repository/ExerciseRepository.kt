package ru.vafeen.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.models.Exercise

/**
 * Репозиторий для работы с упражнениями в локальной базе данных.
 */
interface ExerciseRepository {
    /**
     * Возвращает поток со списком всех упражнений.
     */
    fun getAllExercises(): Flow<List<Exercise>>

    /**
     * Вставляет список упражнений в базу данных.
     */
    suspend fun insert(exercises: List<Exercise>)
    suspend fun delete(exercises: List<Exercise>)
}