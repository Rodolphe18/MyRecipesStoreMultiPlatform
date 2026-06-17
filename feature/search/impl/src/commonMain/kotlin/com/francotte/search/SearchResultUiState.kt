package com.francotte.search

import com.francotte.model.LikeableRecipe

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState

    /**
     * The query is empty or too short. Distinguished from "no results" so the screen can show the
     * search-mode entry instead of an empty-results state.
     */
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState

    data class Success(
        val categories: List<String> = emptyList(),
        val areas: List<String> = emptyList(),
        val ingredients: List<String>,
        val likeableRecipes: List<LikeableRecipe>,
    ) : SearchResultUiState {
        fun isEmpty(): Boolean =
            categories.isEmpty() && areas.isEmpty() && ingredients.isEmpty() && likeableRecipes.isEmpty()
    }

    /** The search index isn't populated yet (driven by [SearchContentsRepository.searchContentsIsReady]). */
    data object SearchNotReady : SearchResultUiState
}
