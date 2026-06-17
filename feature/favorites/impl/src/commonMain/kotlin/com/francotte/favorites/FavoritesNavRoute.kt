package com.francotte.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.favorites.api.FavoritesNavKey
import com.francotte.model.LikeableRecipe
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.favoritesScreen(
    onRecipeClick: (List<String>, Int, String) -> Unit,
    onCustomRecipeClick: (String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
) {
    composable<FavoritesNavKey> {
        FavoriteRoute(
            onRecipeClick = onRecipeClick,
            onCustomRecipeClick = onCustomRecipeClick,
            onToggleFavorite = onToggleFavorite,
        )
    }
}

@Composable
internal fun FavoriteRoute(
    onRecipeClick: (List<String>, Int, String) -> Unit,
    onCustomRecipeClick: (String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is FavoritesEvent.NavigateToRecipe -> onRecipeClick(event.ids, event.index, event.title)
                is FavoritesEvent.NavigateToCustomRecipe -> onCustomRecipeClick(event.recipeId)
            }
        }
    }

    FavoritesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is FavoritesAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
