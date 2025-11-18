package ru.vafeen.data.network.dto.github_service

import com.google.gson.annotations.SerializedName

/**
 * Данные об файле (asset) из GitHub API.
 *
 * @property size Размер файла в байтах.
 * @property browserDownloadUrl URL для загрузки файла через браузер.
 */
internal data class AssetDTO(
    @SerializedName("size") val size: Long,
    @SerializedName("browser_download_url") val browserDownloadUrl: String
)
