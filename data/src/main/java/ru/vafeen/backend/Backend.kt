package ru.vafeen.backend

import kotlinx.coroutines.delay
import ru.vafeen.backend.Backend.userDatabase
import ru.vafeen.domain.models.SignResult
import ru.vafeen.domain.network.result.ResponseResult
import java.util.concurrent.ConcurrentHashMap

/**
 * Простой in-memory бэкенд для аутентификации пользователей.
 *
 * Хранит пользователей в thread-safe карте [userDatabase] с ключом по [username].
 * Поддерживает регистрацию и вход с валидацией уникальности имени и проверкой пароля.
 * Генерирует случайные токены доступа и обновления для демо-целей.
 */
internal object Backend {

    /**
     * In-memory хранилище пользователей: username → UserData.
     * Используется [ConcurrentHashMap] для потокобезопасности.
     */
    private val userDatabase = ConcurrentHashMap<String, UserData>()

    suspend fun serverDelay() {
        delay(2000)
    }

    /**
     * Регистрирует нового пользователя с заданными данными.
     *
     * Проверяет, что [username] ещё не занят.
     * Хэширует пароль с использованием BCrypt.
     * Генерирует случайные токены доступа и обновления.
     *
     * @param name Полное имя пользователя.
     * @param username Уникальное имя для входа.
     * @param password Пароль в открытом виде (будет хэширован).
     * @return [ResponseResult.Success] с [SignResult] при успешной регистрации,
     *         [ResponseResult.Error] с исключением, если username уже занят или возникла ошибка.
     */
    suspend fun signUp(
        name: String,
        username: String,
        password: String
    ): ResponseResult<SignResult> {
        serverDelay()
        return try {
            if (userDatabase.containsKey(username)) {
                throw IllegalArgumentException("Пользователь с таким именем уже существует")
            }

            val userId = synchronized(this) {
                userDatabase[username]?.let {
                    throw IllegalStateException("Гонка при создании пользователя") // защита от редкого race condition
                }
                val id = System.currentTimeMillis()
                val passwordHash = PasswordHasher.passwordToHash(password)
                val userData =
                    UserData(id = id, username = username, passwordHash = passwordHash, name = name)
                userDatabase[username] = userData
                id
            }

            val accessToken = generateToken()
            val refreshToken = generateToken()

            ResponseResult.Success(SignResult(userId, accessToken, refreshToken))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }

    /**
     * Выполняет вход существующего пользователя по [username] и [password].
     *
     * Проверяет наличие пользователя и корректность пароля (через BCrypt.verify).
     * Генерирует новые токены при успешной аутентификации.
     *
     * @param username Имя пользователя.
     * @param password Пароль в открытом виде.
     * @return [ResponseResult.Success] с [SignResult] при успешном входе,
     *         [ResponseResult.Error] с исключением, если пользователь не найден или пароль неверен.
     */
    suspend fun signIn(username: String, password: String): ResponseResult<SignResult> {
        serverDelay()
        return try {
            val userData = userDatabase[username]
                ?: throw IllegalArgumentException("Пользователь не найден")

            val isPasswordValid = userData.passwordHash == PasswordHasher.passwordToHash(password)
            if (!isPasswordValid) {
                throw IllegalArgumentException("Неверный пароль")
            }

            val accessToken = generateToken()
            val refreshToken = generateToken()

            ResponseResult.Success(SignResult(userData.id, accessToken, refreshToken))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }

    /**
     * Генерирует случайный токен длиной 32 символа (Base64-безопасный).
     *
     * @return Случайная строка-токен.
     */
    private fun generateToken(): String {
        return (1..32)
            .map { ('a'..'z').random() }
            .joinToString("")
    }
}