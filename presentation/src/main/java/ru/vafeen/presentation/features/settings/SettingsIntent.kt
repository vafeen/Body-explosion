package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.Training
import java.time.LocalTime

/**
 * Запечатанный интерфейс, представляющий намерения пользователя на экране настроек.
 */
internal sealed interface SettingsIntent {
    /**
     * Намерение обновить настройки.
     *
     * @property updating Лямбда-выражение, которое принимает текущие настройки и возвращает обновленные.
     */
    data class UpdateSettings(val updating: (Settings) -> Settings) : SettingsIntent


    /**
     * Намерение обновить информацию о тренировке.
     *
     * @property training Обновленная информация о тренировке.
     */
    data class UpdateTraining(val training: Training) : SettingsIntent
    data object SwitchExerciseDurationDialogIsShowed : SettingsIntent
    data object SwitchBreakDurationDialogIsShowed : SettingsIntent
    data object ApplyExerciseDuration : SettingsIntent
    data class UpdateTempExerciseDuration(val duration: LocalTime) : SettingsIntent
    data object ResetExerciseDuration : SettingsIntent
    data object ApplyBreakDuration : SettingsIntent
    data class UpdateTempBreakDuration(val duration: LocalTime) : SettingsIntent
    data object ResetBreakDuration : SettingsIntent
}