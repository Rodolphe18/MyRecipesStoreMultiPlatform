package com.francotte.feature.profile.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object ProfileNavKey : NavKey

fun Navigator.navigateToProfile() {
    navigate(ProfileNavKey)
}
