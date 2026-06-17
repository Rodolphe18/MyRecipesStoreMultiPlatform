package com.francotte.feature.reset.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object RequestResetNavKey : NavKey

fun Navigator.navigateToRequestReset() {
    navigate(RequestResetNavKey)
}

@Serializable
data class ResetPasswordNavKey(val token: String) : NavKey

fun Navigator.navigateToResetPassword(token: String) {
    navigate(ResetPasswordNavKey(token))
}
