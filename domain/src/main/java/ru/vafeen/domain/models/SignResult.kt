package ru.vafeen.domain.models

data class SignResult(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
