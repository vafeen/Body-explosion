package ru.vafeen.presentation.features.training

/**
 * Представляет одноразовые эффекты, которые могут быть отправлены из ViewModel в UI.
 */
internal sealed interface TrainingEffect {
    /**
     * Показывает всплывающее сообщение (Toast).
     *
     * @property message Сообщение для отображения.
     * @property length Длительность отображения сообщения (например, Toast.LENGTH_SHORT).
     */
    data class ShowToast(val message: String, val length: Int) : TrainingEffect
}
