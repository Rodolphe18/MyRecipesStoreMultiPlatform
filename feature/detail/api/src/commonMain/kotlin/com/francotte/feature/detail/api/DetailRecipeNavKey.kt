package com.francotte.feature.detail.api

import com.francotte.navigation.NavKey
import com.francotte.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class DetailRecipeNavKey(
    val ids: List<String>? = null,
    val index: Int? = null,
    val title: String? = null,
) : NavKey

fun Navigator.navigateToDetail(ids: List<String>, index: Int, title: String) {
    navigate(DetailRecipeNavKey(ids, index, title))
}
