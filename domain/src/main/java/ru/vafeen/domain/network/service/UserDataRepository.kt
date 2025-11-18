package ru.vafeen.domain.network.service

import ru.vafeen.domain.models.SignResult
import ru.vafeen.domain.network.result.ResponseResult

interface UserDataRepository {
    suspend fun signUp(name: String, username: String, password: String): ResponseResult<SignResult>
    suspend fun signIn(username: String, password: String): ResponseResult<SignResult>
}