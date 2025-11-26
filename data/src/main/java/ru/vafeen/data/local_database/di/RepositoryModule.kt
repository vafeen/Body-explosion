package ru.vafeen.data.local_database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.local_database.repository.RoomExerciseRepository
import ru.vafeen.data.local_database.repository.RoomUserDataRepository
import ru.vafeen.data.local_database.repository.RoomWorkoutRepository
import ru.vafeen.domain.repository.ExerciseRepository
import ru.vafeen.domain.repository.UserDataRepository
import ru.vafeen.domain.repository.WorkoutRepository


@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindsTrainingRepository(roomTrainingRepository: RoomExerciseRepository): ExerciseRepository

    @Binds
    fun bindsUserDataRepository(roomUserDataRepository: RoomUserDataRepository): UserDataRepository

    @Binds
    fun bindsWorkoutRepository(roomWorkoutRepository: RoomWorkoutRepository): WorkoutRepository
}