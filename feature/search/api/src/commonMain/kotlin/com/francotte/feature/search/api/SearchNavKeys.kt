package com.francotte.feature.search.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
enum class SearchMode(val title: String) {
    INGREDIENTS("Ingredients"),
    COUNTRY("Countries"),
    CATEGORIES("Categories"),
}

@Serializable
object SearchNavKey : NavKey

fun Navigator.navigateToSearch() {
    navigateTopLevel(SearchNavKey)
}

@Serializable
data class SearchModeNavKey(val searchMode: SearchMode) : NavKey

fun Navigator.navigateToSearchMode(searchMode: SearchMode) {
    navigate(SearchModeNavKey(searchMode))
}

@Serializable
data class SearchRecipesNavKey(val item: String, val mode: SearchMode) : NavKey

fun Navigator.navigateToSearchRecipes(item: String, mode: SearchMode) {
    navigate(SearchRecipesNavKey(item, mode))
}
