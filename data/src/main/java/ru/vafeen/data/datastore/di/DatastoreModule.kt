package ru.vafeen.data.datastore.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.datastore.SettingsManagerImpl
import ru.vafeen.domain.datastore.SettingsManager
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal class DatastoreModule {
    @Provides
    @Singleton
    fun provideSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            SettingsManagerImpl.SETTINGS_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
}
