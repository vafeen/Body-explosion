package ru.vafeen.domain.models


data class Settings(
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
) {
    companion object {
        fun default() = Settings(
            user = null,
            accessToken = null,
            refreshToken = null,
        )
    }
}

