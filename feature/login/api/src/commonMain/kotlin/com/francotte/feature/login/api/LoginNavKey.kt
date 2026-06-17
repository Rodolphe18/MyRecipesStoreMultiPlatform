package com.francotte.feature.login.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object LoginNavKey : NavKey

fun Navigator.navigateToLogin() {
    navigateTopLevel(LoginNavKey)
}
