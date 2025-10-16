package ru.vafeen.data.network.repository

import android.content.Context
import android.vafeen.direct_refresher.DirectRefresher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import ru.vafeen.data.network.converters.toStatus
import ru.vafeen.domain.network.service.Refresher
import javax.inject.Inject
import android.vafeen.direct_refresher.refresher.Refresher as LibRefresher

internal class RefresherImpl @Inject constructor(
    @ApplicationContext context: Context
) : Refresher {
    private val libRefresher: LibRefresher =
        DirectRefresher.provideRefresher(
            context = context,
            downloader = DirectRefresher.provideDownloader(
                context = context,
                baseUrl = BASE_LINK
            ),
            installer = DirectRefresher.provideInstaller(context = context)
        )
    override val progressFlow =
        libRefresher.progressFlow.map { it.toStatus() }

    override suspend fun refresh(
        coroutineScope: CoroutineScope,
        url: String,
        downloadedFileName: String
    ) = libRefresher.refresh(coroutineScope, url, downloadedFileName)

    companion object {
        private const val BASE_LINK = "https://github.com/"
    }
}