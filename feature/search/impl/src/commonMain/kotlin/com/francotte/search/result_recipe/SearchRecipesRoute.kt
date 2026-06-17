package com.francotte.search.result_recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.feature.search.api.SearchRecipesNavKey
import com.francotte.model.LikeableRecipe
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.searchRecipesScreen(
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onBack: () -> Unit,
) {
    composable<SearchRecipesNavKey> { entry ->
        val key = entry.toRoute<SearchRecipesNavKey>()
        SearchRecipesRoute(
            navKey = key,
            onOpenRecipe = onOpenRecipe,
            onToggleFavorite = onToggleFavorite,
            onBack = onBack,
        )
    }
}

@Composable
internal fun SearchRecipesRoute(
    navKey: SearchRecipesNavKey,
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: SearchRecipesViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchRecipesEvent.NavigateToRecipe -> onOpenRecipe(event.ids, event.index, event.title)
                SearchRecipesEvent.NavigateBack -> onBack()
            }
        }
    }

    SearchRecipesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SearchRecipesAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> viewModel.onAction(action)
            }
        },
    )
}
