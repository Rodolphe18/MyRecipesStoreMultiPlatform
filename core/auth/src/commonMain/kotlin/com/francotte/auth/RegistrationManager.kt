package com.francotte.auth

import com.francotte.network.api.AuthApi
import com.francotte.network.model.ImageUpload

class RegistrationManager(
    private val api: AuthApi,
    private val authSynchronizer: AuthSynchronizer,
) : RegistrationRepository {

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        image: ImageUpload?,
    ): Result<Unit> =
        try {
            authSynchronizer.handle(
                api.createUser(
                    username = username,
                    email = email,
                    password = password,
                    image = image,
                ),
                operation = AuthOperation.REGISTER,
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
}
