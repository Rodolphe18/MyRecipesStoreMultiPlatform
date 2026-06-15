package com.francotte.network.utils

import com.francotte.common.utils.AppError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

actual fun Throwable.platformNetworkError(): AppError? = when (this) {
    is UnknownHostException -> AppError.NoInternet(this)
    is ConnectException -> AppError.NoInternet(this)
    is SocketTimeoutException -> AppError.Timeout(this)
    is IOException -> AppError.NetworkIO(this)
    else -> null
}
