package ru.vafeen.presentation.features.settings

import ru.vafeen.domain.models.Settings

/**
 * Запечатанный интерфейс, представляющий намерения пользователя на экране настроек.
 */
internal sealed interface AccountIntent {
    /**
     * Намерение обновить настройки.
     *
     * @property updating Лямбда-выражение, которое принимает текущие настройки и возвращает обновленные.
     */
    data class UpdateAccount(val updating: (Settings) -> Settings) : AccountIntent

}