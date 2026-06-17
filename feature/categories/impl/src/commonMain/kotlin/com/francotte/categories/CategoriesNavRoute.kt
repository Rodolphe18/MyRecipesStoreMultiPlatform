package com.francotte.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.categories.api.CategoriesNavKey
import com.francotte.model.Category
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.categoriesScreen(
    onOpenCategory: (Category) -> Unit,
) {
    composable<CategoriesNavKey> {
        CategoriesRoute(onOpenCategory = onOpenCategory)
    }
}

@Composable
internal fun CategoriesRoute(
    onOpenCategory: (Category) -> Unit,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHost = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoriesEvent.NavigateToCategory -> onOpenCategory(event.category)
                is CategoriesEvent.ShowSnackbar -> snackBarHost.showSnackbar(event.message)
            }
        }
    }
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }

    CategoriesScreen(state = state, onAction = viewModel::onAction)
}
