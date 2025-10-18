package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training")
internal data class TrainingEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isIncludedToTraining: Boolean,
)