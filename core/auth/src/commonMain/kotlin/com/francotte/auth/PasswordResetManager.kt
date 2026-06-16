package com.francotte.auth

import com.francotte.network.api.AuthApi
import com.francotte.network.model.EmailRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

class PasswordResetManager(
    private val api: AuthApi,
) : PasswordResetRepository {

    override suspend fun requestPasswordReset(email: String): Result<Unit> =
        try {
            val response = api.requestPasswordReset(EmailRequest(email))
            if (response.status.isSuccess()) Result.success(Unit)
            else Result.failure(Exception("Erreur : ${response.status.value}"))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> =
        try {
            val response = api.resetPassword(
                mapOf(
                    "token" to token,
                    "newPassword" to newPassword,
                ),
            )
            if (response.status.isSuccess()) Result.success(Unit)
            else Result.failure(Exception(response.bodyAsText().ifBlank { "Unknown error" }))
        } catch (e: Exception) {
            Result.failure(e)
        }
}
