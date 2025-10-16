package ru.vafeen.data.local_database

import ru.vafeen.domain.local_database.TestLocalRepository
import javax.inject.Inject

/**
 * Реализация [TestLocalRepository] с использованием Room.
 *
 * @property db Экземпляр базы данных [AppDatabase].
 */
internal class RoomTestLocalRepository @Inject constructor(
    private val db: AppDatabase
) : TestLocalRepository
