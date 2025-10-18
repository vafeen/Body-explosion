package ru.vafeen.data.local_database.converters

import ru.vafeen.data.local_database.entity.TrainingEntity
import ru.vafeen.domain.models.Training


internal fun TrainingEntity.toTraining(): Training =
    Training(id = id, name = name, isIncludedToTraining = isIncludedToTraining)

internal fun Training.toTrainingEntity(): TrainingEntity =
    TrainingEntity(id = id, name = name, isIncludedToTraining = isIncludedToTraining)
