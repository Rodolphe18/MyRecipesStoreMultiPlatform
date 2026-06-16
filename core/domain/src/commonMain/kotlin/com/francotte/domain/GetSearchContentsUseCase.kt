package com.francotte.domain

import com.francotte.data.interfaces.SearchContentsRepository
import com.francotte.data.interfaces.UserDataRepository
import com.francotte.model.SearchResult
import com.francotte.model.UserData
import com.francotte.model.UserSearchResult
import com.francotte.model.mapToLikeableLightRecipes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * A use case which returns the searched contents matched with the search query.
 */
class GetSearchContentsUseCase(
    private val searchContentsRepository: SearchContentsRepository,
    private val userDataRepository: UserDataRepository,
) {

    operator fun invoke(
        searchQuery: String,
    ): Flow<UserSearchResult> =
        searchContentsRepository.searchContents(searchQuery)
            .mapToUserSearchResult(userDataRepository.userData)
}

private fun Flow<SearchResult>.mapToUserSearchResult(userDataStream: Flow<UserData>): Flow<UserSearchResult> =
    combine(userDataStream) { searchResult, userData ->
        UserSearchResult(
            categories = searchResult.categories,
            ingredients = searchResult.ingredients,
            areas = searchResult.areas,
            likeableRecipes = searchResult.lightRecipes.mapToLikeableLightRecipes(userData),
        )
    }
