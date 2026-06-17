package com.francotte.feature.register.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object RegisterNavKey : NavKey

fun Navigator.navigateToRegister() {
    navigate(RegisterNavKey)
}
