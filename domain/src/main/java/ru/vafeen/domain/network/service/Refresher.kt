package ru.vafeen.domain.network.service

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.network.result.DownloadStatus

/**
 * Интерфейс для обновления приложений Android путем загрузки и установки APK-файлов.
 */
interface Refresher {

    /**
     * Поток, который передает прогресс процесса загрузки.
     * Он выдает обновления о статусе загрузки, такие как "начата", "в процессе", "успешно" или "ошибка".
     */
    val progressFlow: Flow<DownloadStatus>

    /**
     * Начинает загрузку APK-файла с предоставленного URL-адреса и устанавливает его после загрузки.
     * Эта функция загружает файл и отслеживает ход загрузки.
     *
     * @param url URL-адрес APK-файла для загрузки.
     * @param downloadedFileName Имя, под которым будет сохранен загруженный APK-файл.
     */
    suspend fun refresh(url: String, downloadedFileName: String)

    companion object {
        const val APK_FILE_NAME = "app-release.apk"
    }
}
