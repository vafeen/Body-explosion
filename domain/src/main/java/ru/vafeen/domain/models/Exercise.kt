package ru.vafeen.domain.models

import java.time.LocalTime

/**
 * Модель данных, представляющая упражнение.
 *
 * @property id Уникальный идентификатор упражнения.
 * @property name Название упражнения.
 * @property isIncludedToTraining Включено ли упражнение в тренировку.
 */
data class Exercise(
    val id: Int,
    val name: String,
    val isIncludedToTraining: Boolean,
    val duration: LocalTime = LocalTime.of(0, 1, 0),
)
