package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.ReleaseRepositoryImpl
import ru.vafeen.domain.network.service.ReleaseRepository

/**
 * Модуль Dagger для предоставления зависимостей репозиториев.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class RepositoryModule {
    /**
     * Связывает реализацию [ReleaseRepositoryImpl] с интерфейсом [ReleaseRepository].
     *
     * @param impl Реализация [ReleaseRepositoryImpl].
     * @return Экземпляр [ReleaseRepository].
     */
    @Binds
    abstract fun bindsReleaseRepository(impl: ReleaseRepositoryImpl): ReleaseRepository
}
