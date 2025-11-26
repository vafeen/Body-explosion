package ru.vafeen.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.models.Workout

interface WorkoutRepository {
    suspend fun insert(workout: Workout)
    fun getAll(): Flow<List<Workout>>
    suspend fun delete(workout: Workout)
}