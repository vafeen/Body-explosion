package ru.vafeen.data.network.service

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.vafeen.domain.models.Message
import ru.vafeen.domain.service.MessageShower
import javax.inject.Inject

/**
 * Реализация интерфейса [MessageShower] для Android-платформы.
 * Использует MutableSharedFlow с неограниченным буфером и стратегией SUSPEND
 * для надёжной доставки всех ошибок без потерь.
 */
internal class AndroidMessageShower @Inject constructor() : MessageShower {

    /**
     * Внутренний буфер для хранения ошибок.
     * Используется стратегия [BufferOverflow.SUSPEND] для предотвращения потери данных.
     */
    private val _uiMessages = MutableSharedFlow<Message>(
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    /**
     * Публичный поток ошибок, доступный только для чтения.
     * Эмитирует ошибки в порядке их поступления.
     */
    override val uiMessages: SharedFlow<Message> = _uiMessages.asSharedFlow()

    /**
     * Отправляет ошибку в общий поток.
     * Блокирует корутину при переполнении буфера (теоретически не должно происходить
     * благодаря неограниченному буферу).
     *
     * @param message Сообщение с ошибкой, подлежащее отправке в UI.
     */
    override suspend fun showMessage(message: Message) = _uiMessages.emit(message)
}
