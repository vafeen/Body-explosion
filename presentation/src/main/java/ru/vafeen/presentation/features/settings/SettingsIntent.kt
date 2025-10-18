package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.Training

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
     * Намерение сбросить настройки длительности к значениям по умолчанию.
     */
    data object ResetDuration : SettingsIntent

    /**
     * Намерение обновить информацию о тренировке.
     *
     * @property training Обновленная информация о тренировке.
     */
    data class UpdateTraining(val training: Training) : SettingsIntent
}