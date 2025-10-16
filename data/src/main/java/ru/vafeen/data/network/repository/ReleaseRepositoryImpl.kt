package ru.vafeen.data.network.repository

import ru.vafeen.data.network.converters.toRelease
import ru.vafeen.data.network.service.GitHubDataService
import ru.vafeen.data.network.utils.getResponseResultWrappedAllErrors
import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.network.service.ReleaseRepository
import javax.inject.Inject

/**
 * Реализация репозитория для получения информации о релизах из GitHub.
 *
 * @property gitHubDataService Сервис для выполнения запросов к API GitHub.
 */
internal class ReleaseRepositoryImpl @Inject constructor(
    private val gitHubDataService: GitHubDataService,
) : ReleaseRepository {

    /**
     * Получение последнего релиза из GitHub.
     *
     * @return Результат запроса, содержащий информацию о релизе или ошибку.
     */
    override suspend fun getLatestRelease(): ResponseResult<Release> =
        getResponseResultWrappedAllErrors {
            // Выполнение запроса
            val response = gitHubDataService.getLatestRelease()
            val release = response.body().toRelease()
            // Проверка на успешный ответ и релиза на null после конвертации
            if (response.isSuccessful && release != null) {
                // Возвращаем успешный результат
                release
            } else {
                // Обработка HTTP ошибки, если статус не успешен
                throw Exception("Ошибка сервера: ${response.code()}")
            }
        }
}