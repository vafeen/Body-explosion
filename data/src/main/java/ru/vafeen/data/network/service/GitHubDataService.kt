package ru.vafeen.data.network.service

import retrofit2.Response
import retrofit2.http.GET
import ru.vafeen.data.network.dto.github_service.ReleaseDTO

/**
 * Интерфейс для определения методов API, связанных с получением данных о релизах на GitHub.
 */
internal interface GitHubDataService {

    /**
     * Получает информацию о последнем релизе.
     *
     * @return [Response] с объектом [ReleaseDTO], содержащим данные о последнем релизе.
     */
    @GET(EndPoint.LATEST_RELEASE_INFO)
    suspend fun getLatestRelease(): Response<ReleaseDTO>

    companion object {
        const val BASE_LINK = "https://api.github.com/"

        private object EndPoint {
            const val LATEST_RELEASE_INFO = "repos/vafeen/test_android_actions/releases/latest"
        }
    }
}
