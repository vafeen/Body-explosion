package ru.vafeen.presentation.common.utils

import android.content.Context
import android.media.MediaPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vafeen.domain.service.MusicPlayer
import ru.vafeen.presentation.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация [MusicPlayer] с использованием Android [MediaPlayer].
 */
internal class AndroidMusicPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
) : MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null

    /**
     * {@inheritDoc}
     */
    override fun restart() {
        mediaPlayer?.run {
            seekTo(0)
            start()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    /**
     * {@inheritDoc}
     */
    override fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.start()
    }

    /**
     * {@inheritDoc}
     */
    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * {@inheritDoc}
     */
    override fun isInitialized(): Boolean = mediaPlayer != null

    /**
     * {@inheritDoc}
     */
    override fun init() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.rammstein_sonne_cut)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface MusicPlayerModule {
    @Binds
    @Singleton
    fun provideMusicPlayer(impl: AndroidMusicPlayer): MusicPlayer
}
