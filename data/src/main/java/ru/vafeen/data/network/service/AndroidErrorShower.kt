package ru.vafeen.data.network.service

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.vafeen.domain.service.ErrorShower
import javax.inject.Inject

/**
 * Реализация интерфейса [ErrorShower] для Android-платформы.
 * Использует MutableSharedFlow с неограниченным буфером и стратегией SUSPEND
 * для надёжной доставки всех ошибок без потерь.
 */
internal class AndroidErrorShower @Inject constructor() : ErrorShower {

    /**
     * Внутренний буфер для хранения ошибок.
     * Используется стратегия [BufferOverflow.SUSPEND] для предотвращения потери данных.
     */
    private val _errors = MutableSharedFlow<Throwable>(
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    /**
     * Публичный поток ошибок, доступный только для чтения.
     *
     * @return SharedFlow, эмитирующий ошибки в порядке их поступления.
     */
    override val errors: SharedFlow<Throwable> = _errors.asSharedFlow()

    /**
     * Отправляет ошибку в общий поток.
     * Блокирует корутину при переполнении буфера (теоретически не должно происходить
     * благодаря неограниченному буферу).
     *
     * @param throwable Исключение, подлежащее отправке в UI.
     */
    override suspend fun showError(throwable: Throwable) = _errors.emit(throwable)
}