package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.network.repository.AndroidUserDataRepository
import ru.vafeen.data.network.repository.ReleaseRepositoryImpl
import ru.vafeen.domain.network.service.ReleaseRepository
import ru.vafeen.domain.network.service.UserDataRepository

/**
 * Модуль Dagger для предоставления зависимостей репозиториев.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface RepositoryModule {
    /**
     * Связывает реализацию [ReleaseRepositoryImpl] с интерфейсом [ReleaseRepository].
     *
     * @param impl Реализация [ReleaseRepositoryImpl].
     * @return Экземпляр [ReleaseRepository].
     */
    @Binds
    fun bindsReleaseRepository(impl: ReleaseRepositoryImpl): ReleaseRepository

    @Binds
    fun bindsUserDataRepository(impl: AndroidUserDataRepository): UserDataRepository
}
