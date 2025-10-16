package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RefresherImpl
import ru.vafeen.data.network.service.GitHubDataService
import ru.vafeen.domain.network.service.Refresher
import javax.inject.Singleton

/**
 * Модуль Dagger для предоставления сетевых сервисов.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class ServicesModule {
    /**
     * Предоставляет экземпляр [GitHubDataService].
     *
     * @return Экземпляр [GitHubDataService].
     */
    @Provides
    @Singleton
    fun providesGitHubDataService(): GitHubDataService = Retrofit.Builder()
        .baseUrl(GitHubDataService.BASE_LINK)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GitHubDataService::class.java)

    /**
     * Предоставляет реализацию [Refresher].
     *
     * @param impl Реализация [RefresherImpl].
     * @return Экземпляр [Refresher].
     */
    @Provides
    @Singleton
    fun provideRefresher(impl: RefresherImpl): Refresher = impl
}
