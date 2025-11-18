package ru.vafeen.data.network.repository

import ru.vafeen.data.network.converters.toRelease
import ru.vafeen.data.network.service.GitHubDataService
import ru.vafeen.data.network.utils.getResponseResultWrappedAllErrors
import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.network.service.ReleaseRepository
import java.io.IOException
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
            if (response.isSuccessful && release != null) {
                release
            } else {
                // Обработка HTTP ошибки, если статус не успешен
                throw IOException("Ошибка сервера: ${response.code()}")
            }
        }
}