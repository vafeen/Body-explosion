package ru.vafeen.data.network.repository

import ru.vafeen.backend.Backend
import ru.vafeen.domain.models.SignResult
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.network.service.UserDataRepository
import javax.inject.Inject

internal class AndroidUserDataRepository @Inject constructor() : UserDataRepository {
    override suspend fun signUp(
        name: String,
        username: String,
        password: String
    ): ResponseResult<SignResult> = Backend.signUp(name, username, password)

    override suspend fun signIn(
        username: String,
        password: String
    ): ResponseResult<SignResult> = Backend.signIn(username, password)
}