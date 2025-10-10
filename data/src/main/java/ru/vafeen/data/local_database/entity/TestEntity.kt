package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tests")
internal data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val data: String
)