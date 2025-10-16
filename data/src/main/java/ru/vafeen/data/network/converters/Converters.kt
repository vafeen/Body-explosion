package ru.vafeen.data.network.converters

import ru.vafeen.data.network.dto.github_service.ReleaseDTO
import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.DownloadStatus
import android.vafeen.direct_refresher.downloader.DownloadStatus as LibDownloadStatus


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

internal fun LibDownloadStatus.toStatus(): DownloadStatus = when (this) {
    is LibDownloadStatus.Error -> DownloadStatus.Error(exception)
    is LibDownloadStatus.InProgress -> DownloadStatus.InProgress(percentage)
    LibDownloadStatus.Started -> DownloadStatus.Started
    LibDownloadStatus.Success -> DownloadStatus.Success
}