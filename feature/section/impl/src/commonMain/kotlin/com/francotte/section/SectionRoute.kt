package com.francotte.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.section.api.SectionNavKey
import com.francotte.model.LikeableRecipe
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.sectionScreen(
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onBack: () -> Unit,
) {
    composable<SectionNavKey> { entry ->
        val key = entry.toRoute<SectionNavKey>()
        SectionRoute(
            navKey = key,
            onToggleFavorite = onToggleFavorite,
            onOpenRecipe = onOpenRecipe,
            onBackClick = onBack,
        )
    }
}

@Composable
internal fun SectionRoute(
    navKey: SectionNavKey,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    onOpenRecipe: (List<String>, Int, String) -> Unit,
    onBackClick: () -> Unit,
) {
    val sectionViewModel: SectionViewModel = koinViewModel { parametersOf(navKey) }
    val state by sectionViewModel.state.collectAsStateWithLifecycle()
    val snackBarHost = LocalSnackbarHostState.current

    LaunchedEffect(sectionViewModel) {
        sectionViewModel.events.collect { event ->
            when (event) {
                is SectionEvent.NavigateToRecipe -> onOpenRecipe(event.ids, event.index, event.title)
                SectionEvent.NavigateBack -> onBackClick()
                is SectionEvent.ShowSnackbar -> snackBarHost.showSnackbar(event.message)
            }
        }
    }
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }

    VerticalSectionScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SectionAction.OnToggleFavorite -> onToggleFavorite(action.recipe)
                else -> sectionViewModel.onAction(action)
            }
        },
    )
}
