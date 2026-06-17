package com.francotte.search.result_mode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.feature.search.api.SearchMode
import com.francotte.feature.search.api.SearchModeNavKey
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.searchModeScreen(
    onItemSelected: (String, SearchMode) -> Unit,
    onBack: () -> Unit,
) {
    composable<SearchModeNavKey> { entry ->
        val key = entry.toRoute<SearchModeNavKey>()
        SearchModeRoute(navKey = key, onItemSelected = onItemSelected, onBack = onBack)
    }
}

@Composable
internal fun SearchModeRoute(
    navKey: SearchModeNavKey,
    onItemSelected: (String, SearchMode) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: SearchModeViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHost = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchModeEvent.NavigateToRecipes -> onItemSelected(event.item, event.mode)
                SearchModeEvent.NavigateBack -> onBack()
                is SearchModeEvent.ShowSnackbar -> snackBarHost.showSnackbar(event.message)
            }
        }
    }

    ItemSelectionGrid(state = state, onAction = viewModel::onAction)
}
