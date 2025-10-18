package ru.vafeen.domain.models

/**
 * Модель данных, представляющая упражнение.
 *
 * @property id Уникальный идентификатор упражнения.
 * @property name Название упражнения.
 * @property isIncludedToTraining Включено ли упражнение в тренировку.
 */
data class Training(
    val id: Int,
    val name: String,
    val isIncludedToTraining: Boolean,
)
