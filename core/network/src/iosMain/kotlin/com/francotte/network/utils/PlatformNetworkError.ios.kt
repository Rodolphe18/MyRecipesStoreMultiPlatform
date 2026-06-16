package com.francotte.network.utils

import com.francotte.common.utils.AppError

// Ktor-common exceptions (timeouts, ResponseException, SerializationException) are already
// handled in commonMain; anything else falls back to AppError.Unknown.
actual fun Throwable.platformNetworkError(): AppError? = null
