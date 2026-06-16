package com.francotte.auth

import com.francotte.auth.strategy.LoginCredentials
import com.francotte.network.model.ImageUpload
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val isAuthenticated: StateFlow<Boolean>
    val authEvents: SharedFlow<AuthEvent>
    suspend fun login(credentials: LoginCredentials): Result<Unit>
    suspend fun logout()
    suspend fun deleteAccount()
    suspend fun updateUserInfo(userName: String?, image: ImageUpload?)
}
