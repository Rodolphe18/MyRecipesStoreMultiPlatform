package com.francotte.auth.strategy

import com.francotte.auth.AuthSynchronizer
import com.francotte.network.api.AuthApi
import com.francotte.network.model.GoogleIdTokenRequest
import io.ktor.http.isSuccess

class GoogleLoginStrategy(
    private val api: AuthApi,
    private val authSynchronizer: AuthSynchronizer,
) : LoginStrategy<GoogleCredentials> {

    override fun toTypedOrNull(credentials: LoginCredentials): GoogleCredentials? =
        credentials as? GoogleCredentials

    override suspend fun authenticate(credentials: GoogleCredentials): Result<Unit> =
        try {
            val request = GoogleIdTokenRequest(credentials.idToken)
            val response = api.authGoogle(request)
            when {
                response.status.isSuccess() -> authSynchronizer.handle(response)
                response.status.value == 404 -> authSynchronizer.handle(api.createGoogle(request))
                else -> Result.failure(Exception("Google auth failed: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
