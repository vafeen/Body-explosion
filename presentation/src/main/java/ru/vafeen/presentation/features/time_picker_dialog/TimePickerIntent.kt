package ru.vafeen.presentation.features.time_picker_dialog

import java.time.LocalTime

/**
 * Определяет возможные пользовательские действия (интенты)
 * при работе с диалогом выбора времени.
 */
internal sealed interface TimePickerIntent {

    /**
     * Обновление текущего выбранного времени.
     *
     * @property time Новое время, выбранное пользователем.
     */
    data class UpdateTime(val time: LocalTime) : TimePickerIntent

    /**
     * Подтверждение выбранного времени и закрытие диалога.
     */
    data object Confirm : TimePickerIntent

    /**
     * Отмена выбора и закрытие диалога без сохранения изменений.
     */
    data object Cancel : TimePickerIntent
}
