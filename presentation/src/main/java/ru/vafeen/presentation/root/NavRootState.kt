package ru.vafeen.presentation.root

import ru.vafeen.domain.models.Release

/**
 * Состояние корневого экрана приложения.
 *
 * @property isUpdateNeeded Флаг, указывающий, требуется ли обновление приложения.
 * @property release Информация о последнем доступном релизе.
 * @property isUpdateInProcess Флаг, указывающий, идет ли в данный момент процесс обновления.
 * @property percentage Прогресс загрузки обновления в процентах (от 0.0 до 1.0).
 */
data class NavRootState(
    val isUpdateNeeded: Boolean = false,
    val release: Release? = null,
    val isUpdateInProcess: Boolean = false,
    val percentage: Float = 0f,
)
