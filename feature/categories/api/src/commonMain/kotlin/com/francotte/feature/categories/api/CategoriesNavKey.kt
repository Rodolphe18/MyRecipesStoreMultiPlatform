package com.francotte.feature.categories.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object CategoriesNavKey : NavKey

fun Navigator.navigateToCategories() {
    navigateTopLevel(CategoriesNavKey)
}

@Serializable
data class CategoryNavKey(val category: String) : NavKey

fun Navigator.navigateToCategory(category: String) {
    navigate(CategoryNavKey(category))
}
