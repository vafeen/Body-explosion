package ru.vafeen.data.network.utils

import ru.vafeen.domain.network.result.ResponseResult


/**
 * Функция-обертка для безопасного выполнения сетевых запросов с комплексной обработкой ошибок.
 *
 * Эта функция высшего порядка:
 * 1. Выполняет предоставленный сетевой запрос в блоке try-catch.
 * 2. Возвращает успешный результат, обернутый в [ResponseResult.Success], если выполнение прошло успешно.
 * 3. Перехватывает все исключения и возвращает их как [ResponseResult.Error].
 * 4. Логирует полную трассировку стека ошибок для отладки.
 *
 * @param T Тип ожидаемого успешного ответа.
 * @param response Приостанавливающая лямбда, содержащая логику сетевого запроса.
 * @return [ResponseResult], содержащий либо:
 * - [ResponseResult.Success] с данными ответа при успешном выполнении
 * - [ResponseResult.Error] с деталями ошибки, если возникает какое-либо исключение
 */
internal suspend fun <T> getResponseResultWrappedAllErrors(response: suspend () -> T): ResponseResult<T> =
    try {
        ResponseResult.Success(data = response())
    } catch (e: Exception) {
        ResponseResult.Error(
            Exception("${e.javaClass.simpleName}: ${e.localizedMessage}")
        )
    }