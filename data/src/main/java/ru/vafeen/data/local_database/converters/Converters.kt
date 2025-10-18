package ru.vafeen.data.local_database.converters

import ru.vafeen.data.local_database.entity.TrainingEntity
import ru.vafeen.domain.models.Training

/**
 * Конвертирует [TrainingEntity] в [Training].
 */
internal fun TrainingEntity.toTraining(): Training =
    Training(id = id, name = name, isIncludedToTraining = isIncludedToTraining)

/**
 * Конвертирует [Training] в [TrainingEntity].
 */
internal fun Training.toTrainingEntity(): TrainingEntity =
    TrainingEntity(id = id, name = name, isIncludedToTraining = isIncludedToTraining)
