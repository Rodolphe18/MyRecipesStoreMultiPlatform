package com.francotte.network.api

import com.francotte.network.model.AuthRequest
import com.francotte.network.model.AuthResponse
import com.francotte.network.model.EmailRequest
import com.francotte.network.model.GoogleIdTokenRequest
import com.francotte.network.model.ImageUpload
import com.francotte.network.utils.appendImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class AuthApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun createUser(
        username: String,
        email: String,
        password: String,
        image: ImageUpload?,
    ): AuthResponse =
        client.post("${baseUrl}users/create") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("username", username)
                        append("email", email)
                        append("password", password)
                        image?.let { appendImage(it) }
                    },
                ),
            )
        }.body()

    suspend fun updateUserProfile(
        token: String,
        username: String?,
        image: ImageUpload?,
    ): AuthResponse =
        client.put("${baseUrl}users/update-profile") {
            header(HttpHeaders.Authorization, token)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        username?.let { append("username", it) }
                        image?.let { appendImage(it) }
                    },
                ),
            )
        }.body()

    suspend fun deleteUser(userId: Long) {
        client.delete("${baseUrl}users/$userId")
    }

    suspend fun authUser(request: AuthRequest): AuthResponse =
        client.post("${baseUrl}users/auth") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun requestPasswordReset(request: EmailRequest): HttpResponse =
        client.post("${baseUrl}users/reset-password-request") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

    suspend fun resetPassword(data: Map<String, String>): HttpResponse =
        client.post("${baseUrl}users/reset-password/confirm") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }

    suspend fun createGoogle(request: GoogleIdTokenRequest): AuthResponse =
        client.post("${baseUrl}users/create/google") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun authGoogle(request: GoogleIdTokenRequest): AuthResponse =
        client.post("${baseUrl}users/auth/google") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}
