package ru.vafeen.domain.network.result

/**
 * Запечатанный класс для представления статуса операции загрузки.
 */
sealed class DownloadStatus {

    /**
     * Представляет начало процесса загрузки.
     */
    data object Started : DownloadStatus()

    /**
     * Представляет ход загрузки с текущим процентным соотношением.
     *
     * @property percentage Процент загруженного файла, выраженный в виде числа с плавающей запятой от 0.0 до 1.0.
     */
    class InProgress(val percentage: Int) : DownloadStatus()

    /**
     * Указывает на успешное завершение процесса загрузки.
     */
    data object Success : DownloadStatus()

    /**
     * Представляет ошибку, возникшую в процессе загрузки.
     *
     * @property exception Исключение, описывающее ошибку.
     */
    class Error(val exception: Exception) : DownloadStatus()
}
