package com.francotte.network.api

import com.francotte.network.model.AuthRequest
import com.francotte.network.model.EmailRequest
import com.francotte.network.model.GoogleIdTokenRequest
import com.francotte.network.model.ImageUpload
import com.francotte.network.utils.appendImage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
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

/**
 * Auth endpoints return the raw [HttpResponse]: the auth flow drives control flow from HTTP status
 * codes (200/202/404/409/413), so non-2xx responses must be inspected rather than thrown. The shared
 * client has `expectSuccess = true`, so [captureResponse] catches the [ResponseException] and unwraps
 * its [HttpResponse]; genuine transport errors (no response) still propagate.
 */
class AuthApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    private suspend inline fun captureResponse(crossinline call: suspend () -> HttpResponse): HttpResponse =
        try {
            call()
        } catch (e: ResponseException) {
            e.response
        }

    suspend fun createUser(
        username: String,
        email: String,
        password: String,
        image: ImageUpload?,
    ): HttpResponse = captureResponse {
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
        }
    }

    suspend fun updateUserProfile(
        token: String,
        username: String?,
        image: ImageUpload?,
    ): HttpResponse = captureResponse {
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
        }
    }

    suspend fun deleteUser(userId: Long) {
        client.delete("${baseUrl}users/$userId")
    }

    suspend fun authUser(request: AuthRequest): HttpResponse = captureResponse {
        client.post("${baseUrl}users/auth") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun requestPasswordReset(request: EmailRequest): HttpResponse = captureResponse {
        client.post("${baseUrl}users/reset-password-request") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun resetPassword(data: Map<String, String>): HttpResponse = captureResponse {
        client.post("${baseUrl}users/reset-password/confirm") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    suspend fun createGoogle(request: GoogleIdTokenRequest): HttpResponse = captureResponse {
        client.post("${baseUrl}users/create/google") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun authGoogle(request: GoogleIdTokenRequest): HttpResponse = captureResponse {
        client.post("${baseUrl}users/auth/google") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}
