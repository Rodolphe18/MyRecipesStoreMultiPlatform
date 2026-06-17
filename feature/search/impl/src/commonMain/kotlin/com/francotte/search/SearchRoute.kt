package com.francotte.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.search.api.SearchMode
import com.francotte.feature.search.api.SearchNavKey
import com.francotte.model.LikeableRecipe
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.searchScreen(
    onSearchModeSelected: (SearchMode) -> Unit,
    onSearchTypeClick: (String, SearchMode) -> Unit,
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
) {
    composable<SearchNavKey> {
        SearchRoute(
            onSearchModeSelected = onSearchModeSelected,
            onSearchTypeClick = onSearchTypeClick,
            onOpenRecipe = onOpenRecipe,
            onToggleFavorite = onToggleFavorite,
        )
    }
}

@Composable
internal fun SearchRoute(
    onSearchModeSelected: (SearchMode) -> Unit,
    onSearchTypeClick: (String, SearchMode) -> Unit,
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchEvent.NavigateToSearchMode -> onSearchModeSelected(event.mode)
                is SearchEvent.NavigateToSearchRecipes -> onSearchTypeClick(event.item, event.mode)
                is SearchEvent.NavigateToRecipe -> onOpenRecipe(event.ids, event.index, event.title)
            }
        }
    }

    SearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SearchAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
