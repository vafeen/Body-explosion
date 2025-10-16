package ru.vafeen.domain.network.service

import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.ResponseResult

/**
 * Интерфейс репозитория для работы с релизами.
 */
interface ReleaseRepository {

    /**
     * Получает информацию о последнем релизе.
     *
     * @return [ResponseResult] с результатом запроса, содержащим объект [Release].
     */
    suspend fun getLatestRelease(): ResponseResult<Release>
}
