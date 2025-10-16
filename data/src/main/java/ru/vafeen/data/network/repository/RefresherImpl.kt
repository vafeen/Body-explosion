package ru.vafeen.data.network.repository

import android.content.Context
import android.util.Log
import android.vafeen.direct_refresher.DirectRefresher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.vafeen.data.network.converters.toStatus
import ru.vafeen.domain.network.service.Refresher
import javax.inject.Inject
import android.vafeen.direct_refresher.refresher.Refresher as LibRefresher

/**
 * Реализация интерфейса [Refresher] для обновления приложения.
 *
 * @param context Контекст приложения.
 */
internal class RefresherImpl @Inject constructor(
    @ApplicationContext context: Context
) : Refresher {
    private val libRefresher: LibRefresher =
        DirectRefresher.provideRefresher(
            downloader = DirectRefresher.provideDownloader(
                context = context,
                baseUrl = BASE_LINK,
            ),
            installer = DirectRefresher.provideInstaller(context = context)
        )
    override val progressFlow =
        libRefresher.progressFlow.map { it.toStatus() }.onEach {
            Log.d("refresher", "$it")
        }

    override suspend fun refresh(
        url: String,
        downloadedFileName: String
    ) = libRefresher.refresh(url, downloadedFileName)

    companion object {
        private const val BASE_LINK = "https://github.com/"
    }
}
