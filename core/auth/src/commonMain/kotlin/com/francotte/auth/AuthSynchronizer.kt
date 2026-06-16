package com.francotte.auth

import com.francotte.data.interfaces.UserDataRepository
import com.francotte.data.sync.SyncScheduler
import com.francotte.network.model.AuthResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.francotte.common.di.ioDispatcher
import kotlinx.coroutines.withContext

enum class AuthOperation { LOGIN, REGISTER, UPDATE }

class AuthSynchronizer(
    private val preferences: UserDataRepository,
    private val syncScheduler: SyncScheduler,
    private val eventBus: AuthEventBus,
) {
    suspend fun handle(
        response: HttpResponse,
        operation: AuthOperation = AuthOperation.LOGIN,
    ): Result<Unit> = withContext(ioDispatcher) {
        when (response.status.value) {
            200, 202 -> {
                runCatching { response.body<AuthResponse>() }.getOrNull()?.let { body ->
                    preferences.updateUserInfo(
                        isConnected = true,
                        name = body.user.username ?: "",
                        userId = body.user.userId,
                        userToken = body.token,
                        userEmail = body.user.email ?: "",
                        userImage = body.user.image ?: "",
                    )
                    val event = when (operation) {
                        AuthOperation.LOGIN -> AuthEvent.LoginSuccess(body.user.username ?: "")
                        AuthOperation.REGISTER -> AuthEvent.RegisterSuccess(body.user.username ?: "")
                        AuthOperation.UPDATE -> AuthEvent.UpdateSuccess
                    }
                    eventBus.emit(event)
                }
                if (operation != AuthOperation.UPDATE) {
                    syncScheduler.enqueueForLogin()
                }
                Result.success(Unit)
            }
            413 -> {
                eventBus.emit(AuthEvent.PayloadTooLarge)
                Result.failure(Exception("413"))
            }
            409 -> {
                eventBus.emit(AuthEvent.UserAlreadyExists)
                Result.failure(Exception("409"))
            }
            else -> {
                eventBus.emit(if (operation == AuthOperation.REGISTER) AuthEvent.RegisterFailed else AuthEvent.LoginFailed)
                Result.failure(Exception("${response.status.value}"))
            }
        }
    }
}
