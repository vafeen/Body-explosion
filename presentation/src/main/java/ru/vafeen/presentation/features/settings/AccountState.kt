package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.User

/**
 * Состояние экрана настроек.
 *
 * @property appVersion Версия приложения (код и имя).
 */
data class AccountState(
    val user: User? = null,
    val appVersion: Pair<Int?, String?>,
)
