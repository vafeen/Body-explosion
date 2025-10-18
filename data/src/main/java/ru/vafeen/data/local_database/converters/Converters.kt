package ru.vafeen.data.local_database.converters

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.entity.TrainingEntity
import ru.vafeen.domain.models.Training


internal fun TrainingEntity.toTraining(): Training = Training(id = id, name = name)
internal fun Training.toTrainingEntity(): TrainingEntity = TrainingEntity(id = id, name = name)

internal fun <T, R> Flow<List<T>>.recursiveMap(transform: (T) -> R): Flow<List<R>> =
    map { it.map(transform) }