package ru.vafeen.backend

import java.security.MessageDigest

/**
 * Класс для генерации ХЭШ-кода паролей пользователя с использованием соли
 */
object PasswordHasher {
    /**
     * [SHA-256](https://developer.android.com/privacy-and-security/cryptography#kotlin) - хэширование, рекомендованное Google
     */
    private fun String.getSHA256Hash(): String =
        MessageDigest.getInstance("SHA-256").digest(toByteArray()).fold("") { str, it ->
            str + "%02x".format(it)
        }

    /**
     * Алгоритм генерации "Соли", которая нужна для того,
     * чтобы усложнить подбор пароля
     */
    private fun String.addSaltToMessage(): String {
        return "salt${this}salt"
    }

    /**
     * Функция получения hash'а пароля
     * @param password [Пароль, хэш которого нужно получить]
     */
    fun passwordToHash(password: String): String = password.addSaltToMessage().getSHA256Hash()
}