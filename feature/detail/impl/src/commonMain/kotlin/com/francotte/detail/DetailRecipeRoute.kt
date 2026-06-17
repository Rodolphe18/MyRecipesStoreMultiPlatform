package com.francotte.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.detail.api.DetailRecipeNavKey
import com.francotte.model.LikeableRecipe
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Detail destination for the shared Navigation Compose graph. Reads the type-safe
 * [DetailRecipeNavKey] from the back stack entry and forwards its args to the ViewModel via Koin.
 */
fun NavGraphBuilder.detailScreen(
    onBack: () -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
) {
    composable<DetailRecipeNavKey> { entry ->
        val key = entry.toRoute<DetailRecipeNavKey>()
        DetailRecipeRoute(
            navKey = key,
            onBackClick = onBack,
            onToggleFavorite = onToggleFavorite,
        )
    }
}

@Composable
internal fun DetailRecipeRoute(
    navKey: DetailRecipeNavKey,
    onBackClick: () -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
) {
    val viewModel: DetailRecipeViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                DetailEvent.NavigateBack -> onBackClick()
            }
        }
    }

    DetailRecipeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                // Favorite toggling stays outside the VM (FavoriteManager decoupling).
                is DetailAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
