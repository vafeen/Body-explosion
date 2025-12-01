package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.delay
import ru.vafeen.backend.PasswordHasher
import ru.vafeen.data.local_database.AppDatabase
import ru.vafeen.data.local_database.asList
import ru.vafeen.data.local_database.entity.UserDataEntity
import ru.vafeen.domain.models.SignResult
import ru.vafeen.domain.models.User
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.repository.UserDataRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация [UserDataRepository] для локальной базы данных, использующая Room.
 * Имитирует поведение сетевого репозитория с задержками.
 *
 * @param db Экземпляр базы данных [ru.vafeen.data.local_database.AppDatabase].
 */
@Singleton
internal class RoomUserDataRepository @Inject constructor(
    db: AppDatabase
) : UserDataRepository {
    private val dao = db.userDataDao()

    /**
     * Имитирует задержку сервера.
     */
    private suspend fun serverDelay() = delay(2000)

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param name Имя пользователя.
     * @param username Уникальное имя пользователя (логин).
     * @param password Пароль пользователя.
     * @return [ResponseResult.Success] с [SignResult] в случае успеха,
     * или [ResponseResult.Error] в случае ошибки (например, если пользователь уже существует).
     */
    override suspend fun signUp(
        name: String,
        username: String,
        password: String
    ): ResponseResult<SignResult> {
        serverDelay()
        return try {
            val users = dao.getAll()
            if (users.map { it.username }
                    .contains(username)) {
                throw IllegalArgumentException("Пользователь с таким именем уже существует")
            }

            val passwordHash = PasswordHasher.passwordToHash(password)
            val userDataEntity =
                UserDataEntity(
                    username = username,
                    passwordHash = passwordHash,
                    name = name
                )
            dao.insert(userDataEntity.asList())

            val accessToken = generateToken()
            val refreshToken = generateToken()

            ResponseResult.Success(
                SignResult(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    user = User(name, username)
                )
            )
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }

    /**
     * Аутентифицирует пользователя в системе.
     *
     * @param username Имя пользователя (логин).
     * @param password Пароль пользователя.
     * @return [ResponseResult.Success] с [SignResult] в случае успеха,
     * или [ResponseResult.Error] в случае ошибки (неверный логин или пароль).
     */
    override suspend fun signIn(
        username: String,
        password: String
    ): ResponseResult<SignResult> {
        serverDelay()
        return try {
            val userData = dao.getByUsername(username)
                ?: return ResponseResult.Error(Exception("Пользователь не найден"))

            val isPasswordValid = userData.passwordHash == PasswordHasher.passwordToHash(password)
            if (!isPasswordValid) {
                return ResponseResult.Error(Exception("Неверный пароль"))
            }

            val accessToken = generateToken()
            val refreshToken = generateToken()

            ResponseResult.Success(
                SignResult(
                    accessToken,
                    refreshToken,
                    user = User(userData.name, username)
                )
            )
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
