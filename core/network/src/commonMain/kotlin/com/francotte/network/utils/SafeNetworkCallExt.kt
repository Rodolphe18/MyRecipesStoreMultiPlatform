package com.francotte.network.utils

import com.francotte.common.utils.AppError
import com.francotte.common.utils.DataResult
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeNetworkCall(
    dispatcher: CoroutineDispatcher,
    crossinline call: suspend () -> T,
): DataResult<T> = withContext(dispatcher) {
    try {
        DataResult.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        DataResult.Failure(e.toNetworkError())
    }
}

fun Throwable.toNetworkError(): AppError = when (this) {
    is CancellationException -> throw this
    is ResponseException -> {
        val code = response.status.value
        if (code == 401) AppError.Unauthorized(this) else AppError.Http(code = code, cause = this)
    }
    is HttpRequestTimeoutException -> AppError.Timeout(this)
    is SerializationException -> AppError.Serialization(this)
    else -> platformNetworkError() ?: AppError.Unknown(this)
}

/**
 * Maps platform-specific network exceptions (e.g. JVM `UnknownHostException`,
 * `SocketTimeoutException`) that have no common Ktor type. Returns null when the
 * throwable is not a recognised platform network error.
 */
expect fun Throwable.platformNetworkError(): AppError?
