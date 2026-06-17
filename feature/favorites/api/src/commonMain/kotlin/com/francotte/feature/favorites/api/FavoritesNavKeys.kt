package com.francotte.feature.favorites.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object FavoritesNavKey : NavKey

fun Navigator.navigateToFavorites() {
    navigateTopLevel(FavoritesNavKey)
}

@Serializable
data class CustomRecipeNavKey(val recipeId: String?) : NavKey

fun Navigator.navigateToCustomRecipe(id: String?) {
    navigate(CustomRecipeNavKey(id))
}
