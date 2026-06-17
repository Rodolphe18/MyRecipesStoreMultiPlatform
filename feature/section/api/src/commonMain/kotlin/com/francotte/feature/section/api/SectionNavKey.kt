package com.francotte.feature.section.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class SectionNavKey(val sectionName: String) : NavKey

fun Navigator.navigateToSection(sectionName: String) {
    navigate(SectionNavKey(sectionName))
}
