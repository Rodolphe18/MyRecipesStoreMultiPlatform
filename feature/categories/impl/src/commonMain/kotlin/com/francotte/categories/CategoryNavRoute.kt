package com.francotte.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.categories.api.CategoryNavKey
import com.francotte.model.LikeableRecipe
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.categoryScreen(
    onOpenRecipe: (ids: List<String>, index: Int, title: String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onBack: () -> Unit,
) {
    composable<CategoryNavKey> { entry ->
        val key = entry.toRoute<CategoryNavKey>()
        CategoryRoute(
            navKey = key,
            onOpenRecipe = onOpenRecipe,
            onToggleFavorite = onToggleFavorite,
            onBack = onBack,
        )
    }
}

@Composable
internal fun CategoryRoute(
    navKey: CategoryNavKey,
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: CategoryViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHost = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoryEvent.NavigateToRecipe -> onOpenRecipe(event.ids, event.index, event.title)
                CategoryEvent.NavigateBack -> onBack()
                is CategoryEvent.ShowSnackbar -> snackBarHost.showSnackbar(event.message)
            }
        }
    }
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }

    CategoryScreen(
        state = state,
        onAction = { action ->
            when (action) {
                // Favorite toggling stays outside the VM (FavoriteManager decoupling).
                is CategoryAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> viewModel.onAction(action)
            }
        },
    )
}
