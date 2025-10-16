package ru.vafeen.data.network.converters

import ru.vafeen.data.network.dto.github_service.ReleaseDTO
import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.DownloadStatus
import android.vafeen.direct_refresher.downloader.DownloadStatus as LibDownloadStatus

/**
 * Конвертирует объект [ReleaseDTO] в объект [Release].
 *
 * @return [Release] если [ReleaseDTO] и его APK-ассет не равны null, иначе null.
 */
internal fun ReleaseDTO?.toRelease(): Release? {
    val apk = this?.assets?.find {
        it.browserDownloadUrl.contains(".apk")
    }
    return if (this != null && apk != null) {
        Release(
            tagName = tagName,
            apkUrl = apk.browserDownloadUrl,
            size = apk.size,
            body = body
        )
    } else null
}

/**
 * Конвертирует [LibDownloadStatus] из библиотеки в [DownloadStatus] доменного слоя.
 *
 * @return [DownloadStatus] соответствующий статусу из библиотеки.
 */
internal fun LibDownloadStatus.toStatus(): DownloadStatus = when (this) {
    is LibDownloadStatus.Error -> DownloadStatus.Error(exception)
    is LibDownloadStatus.InProgress -> DownloadStatus.InProgress(percentage)
    LibDownloadStatus.Started -> DownloadStatus.Started
    LibDownloadStatus.Success -> DownloadStatus.Success
}
