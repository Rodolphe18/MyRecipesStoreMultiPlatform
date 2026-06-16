package com.francotte.auth

import com.francotte.network.model.ImageUpload

interface RegistrationRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String,
        image: ImageUpload?,
    ): Result<Unit>
}
