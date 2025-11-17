package ru.vafeen.data.network.dto.github_service

import com.google.gson.annotations.SerializedName

/**
 * Данные о релизе из GitHub API.
 *
 * @property url URL релиза.
 * @property tagName Название тега, связанного с релизом.
 * @property assets Список активов, связанных с релизом.
 * @property body Описание релиза.
 */
internal data class ReleaseDTO(
    @SerializedName("tag_name") val tagName: String,
    @SerializedName("assets") val assets: List<AssetDTO>,
    @SerializedName("body") val body: String?
)
