package ru.vafeen.presentation.features.history

import ru.vafeen.domain.models.Workout

/**
 * Намерения для экрана истории.
 */
internal sealed interface HistoryIntent {
    /**
     * Намерение удалить тренировку.
     * @param workout Тренировка для удаления.
     */
    data class DeleteWorkout(val workout: Workout) : HistoryIntent
}