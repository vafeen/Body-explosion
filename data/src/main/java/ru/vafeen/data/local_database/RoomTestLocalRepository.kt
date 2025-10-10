package ru.vafeen.data.local_database

import ru.vafeen.domain.local_database.TestLocalRepository
import javax.inject.Inject

internal class RoomTestLocalRepository @Inject constructor(
    private val db: AppDatabase
) : TestLocalRepository