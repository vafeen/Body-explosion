package ru.vafeen.domain.models

data class Message(
    val text: String,
    val button: String,
    val clipboardText: String? = null,
)
