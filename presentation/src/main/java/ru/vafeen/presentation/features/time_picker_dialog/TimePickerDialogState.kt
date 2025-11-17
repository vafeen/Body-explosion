package ru.vafeen.presentation.features.time_picker_dialog

import java.time.LocalTime

/**
 * Состояние диалога выбора времени.
 *
 * @property currentTime Текущее выбранное пользователем время.
 */
internal data class TimePickerDialogState(
    val currentTime: LocalTime,
)
