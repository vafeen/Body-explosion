package ru.vafeen.presentation.features.user_sign

/**
 * Data-класс, представляющий состояние экрана входа/регистрации.
 *
 * @property isSignUp Указывает, находится ли экран в режиме регистрации (true) или входа (false).
 * @property name Имя пользователя (используется только при регистрации).
 * @property username Имя пользователя (логин).
 * @property password Пароль.
 * @property isLoading Указывает, выполняется ли в данный момент сетевой запрос.
 * @property error Содержит текст ошибки, если она произошла.
 */
internal data class UserSignState(
    val isSignUp: Boolean = false,
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
