package ru.vafeen.data.local_database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.local_database.RoomTrainingLocalRepository
import ru.vafeen.domain.local_database.TrainingLocalRepository


@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {
    @Provides
    fun provideTrainingLocalRepository(roomTrainingLocalRepository: RoomTrainingLocalRepository): TrainingLocalRepository =
        roomTrainingLocalRepository
}