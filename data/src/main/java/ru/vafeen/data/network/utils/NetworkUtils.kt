package ru.vafeen.data.network.utils

import ru.vafeen.domain.network.result.ResponseResult

/**
 * Функция-обёртка для безопасного выполнения сетевых запросов с дифференцированной обработкой ошибок.
 *
 * Выполняет переданный приостанавливающий блок и возвращает типизированный результат:
 * - [ResponseResult.Success] при успешном завершении,
 * - [ResponseResult.NetworkError] при обнаружении сетевой ошибки (такой как таймаут, отсутствие сети и т.п.),
 * - [ResponseResult.Error] для всех остальных исключений (включая ошибки логики, отмены и системные сбои).
 *
 * Все исходные исключения сохраняются без потерь (включая стектрейс) внутри соответствующего типа ошибки.
 *
 * @param T Тип ожидаемого успешного результата.
 * @param response Блок кода с сетевым запросом.
 * @return Обёрнутый результат выполнения.
 */
internal suspend fun <T> getResponseResultWrappedAllErrors(response: suspend () -> T): ResponseResult<T> =
    try {
        ResponseResult.Success(data = response())
    } catch (throwable: Throwable) {
        val exception = throwable.toExceptionPreservingStackTrace()

        if (throwable.isNetworkRelated()) {
            ResponseResult.NetworkError(exception)
        } else {
            ResponseResult.Error(exception)
        }
    }

/**
 * Преобразует любой [Throwable] в [Exception], сохраняя оригинальное сообщение и полный стектрейс.
 *
 * @return Новый экземпляр [Exception], содержащий:
 * - сообщение в формате: `"SimpleName: original message"`,
 * - стектрейс, идентичный оригинальному [Throwable].
 */
private fun Throwable.toExceptionPreservingStackTrace(): Exception {
    val message = this.message?.let { "${this::class.simpleName}: $it" } ?: this::class.simpleName
    return Exception(message).apply {
        stackTrace = this@toExceptionPreservingStackTrace.stackTrace
    }
}

/**
 * Определяет, является ли данное исключение сетевой ошибкой.
 *
 * Сетевыми считаются:
 * - [java.net.SocketTimeoutException]
 * - [java.net.UnknownHostException]
 * - [java.net.ConnectException]
 * - [java.io.IOException] (включает большинство ошибок OkHttp/Retrofit)
 * - [kotlinx.coroutines.CancellationException] (часто возникает при отмене запроса при уходе со экрана)
 *
 * @return true, если ошибка связана с сетью или подключением, иначе false.
 */
private fun Throwable.isNetworkRelated(): Boolean = when (this) {
    is java.net.SocketTimeoutException,
    is java.net.UnknownHostException,
    is java.net.ConnectException,
    is java.io.IOException,
    is kotlinx.coroutines.CancellationException -> true

    else -> false
}