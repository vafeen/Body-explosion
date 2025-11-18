package ru.vafeen.presentation.features.user_sign

/**
 * Интенты — действия пользователя или системы на экране входа/регистрации.
 */
sealed interface UserSignIntent {

    /**
     * Переключить режим между входом и регистрацией.
     */
    data object ToggleMode : UserSignIntent

    /**
     * Изменено поле "Имя" (только в режиме регистрации).
     */
    data class OnNameChanged(val name: String) : UserSignIntent

    /**
     * Изменено поле "Имя пользователя".
     */
    data class OnUsernameChanged(val username: String) : UserSignIntent

    /**
     * Изменено поле "Пароль".
     */
    data class OnPasswordChanged(val password: String) : UserSignIntent

    /**
     * Нажата кнопка подтверждения («Войти» или «Зарегистрироваться»).
     */
    data object Submit : UserSignIntent
}