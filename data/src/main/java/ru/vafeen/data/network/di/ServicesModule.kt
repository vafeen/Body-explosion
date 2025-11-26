package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.repository.RefresherImpl
import ru.vafeen.data.network.service.AndroidMessageShower
import ru.vafeen.data.network.service.GitHubDataService
import ru.vafeen.domain.network.service.Refresher
import ru.vafeen.domain.service.MessageShower
import javax.inject.Singleton

/**
 * Модуль Dagger для предоставления сетевых сервисов.
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface ServicesModule {
    companion object {
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
    }

    /**
     * Предоставляет реализацию [Refresher].
     *
     * @param impl Реализация [RefresherImpl].
     * @return Экземпляр [Refresher].
     */
    @Binds
    @Singleton
    fun bindsRefresher(impl: RefresherImpl): Refresher

    @Binds
    @Singleton
    fun bindsErrorWShower(impl: AndroidMessageShower): MessageShower
}
