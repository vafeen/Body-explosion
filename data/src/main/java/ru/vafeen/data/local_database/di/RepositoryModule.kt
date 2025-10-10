package ru.vafeen.data.local_database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.local_database.RoomTestLocalRepository
import ru.vafeen.domain.local_database.TestLocalRepository


@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {
    @Provides
    fun provideLocalRepository(roomTestLocalRepository: RoomTestLocalRepository): TestLocalRepository =
        roomTestLocalRepository
}