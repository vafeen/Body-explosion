package ru.vafeen.domain.models

data class SignResult(
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
)
