package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.ReleaseRepositoryImpl
import ru.vafeen.domain.network.service.ReleaseRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindsReleaseRepository(impl: ReleaseRepositoryImpl): ReleaseRepository
}
