package ru.vafeen.presentation.features.user_sign

/**
 * Побочные эффекты, которые [UserSignViewModel] может отправить на UI.
 */
internal sealed interface UserSignEffect {
    /**
     * Эффект, сигнализирующий об успешной аутентификации.
     *
     * @property id Уникальный идентификатор пользователя.
     * @property accessToken Токен доступа.
     * @property refreshToken Токен обновления.
     */
    data class AuthSuccess(
        val id: Long,
        val accessToken: String,
        val refreshToken: String
    ) : UserSignEffect
}
