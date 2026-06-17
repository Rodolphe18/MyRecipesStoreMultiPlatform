package com.francotte.feature.home.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object HomeNavKey : NavKey

fun Navigator.navigateToHome() {
    navigate(HomeNavKey)
}
