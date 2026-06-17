package com.francotte.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.home.api.HomeNavKey
import com.francotte.model.LikeableRecipe
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home destination for the shared Navigation Compose graph. Replaces the former Navigation3
 * `EntryProviderScope.homeEntry`. Navigation targets are passed as lambdas by the app-level
 * NavHost, keeping this feature decoupled from detail/section/video api modules.
 */
fun NavGraphBuilder.homeScreen(
    onRecipeClick: (ids: List<String>, index: Int, title: String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onOpenSection: (String) -> Unit,
    onVideoButtonClick: (String) -> Unit,
) {
    composable<HomeNavKey> {
        HomeRoute(
            onRecipeClick = onRecipeClick,
            onToggleFavorite = onToggleFavorite,
            onOpenSection = onOpenSection,
            onVideoButtonClick = onVideoButtonClick,
        )
    }
}

@Composable
fun HomeRoute(
    onRecipeClick: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onOpenSection: (String) -> Unit,
    onVideoButtonClick: (String) -> Unit,
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = LocalSnackbarHostState.current

    LaunchedEffect(homeViewModel) {
        homeViewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToRecipe -> onRecipeClick(event.ids, event.index, event.title)
                is HomeEvent.NavigateToVideo -> onVideoButtonClick(event.youtubeUrl)
                is HomeEvent.NavigateToSection -> onOpenSection(event.sectionName)
                is HomeEvent.ShowSnackbar -> snackBarHostState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        HomeScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    // Favorite toggling stays outside the VM (FavoriteManager decoupling).
                    is HomeAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                    else -> homeViewModel.onAction(action)
                }
            },
        )
    }
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
