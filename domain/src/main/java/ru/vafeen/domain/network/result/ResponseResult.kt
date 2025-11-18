package ru.vafeen.domain.network.result

/**
 * Запечатанный класс для обработки результатов сетевых запросов.
 *
 * @param T Тип данных, который возвращается в случае успешного выполнения запроса.
 */
sealed class ResponseResult<T> {

    /**
     * Класс, представляющий успешный результат запроса.
     *
     * @property data Данные, полученные в результате успешного выполнения запроса.
     */
    data class Success<T>(val data: T) : ResponseResult<T>()

    /**
     * Класс, представляющий ошибку при выполнении запроса.
     *
     * @property exception Исключение, произошедшее во время выполнения запроса.
     */
    data class Error<T>(val exception: Exception) : ResponseResult<T>()
    data class NetworkError<T>(val exception: Exception) : ResponseResult<T>()
}
