package com.francotte.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.feature.favorites.api.CustomRecipeNavKey
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.customRecipeScreen(
    onBack: () -> Unit,
) {
    composable<CustomRecipeNavKey> { entry ->
        val key = entry.toRoute<CustomRecipeNavKey>()
        CustomRecipeDetailRoute(navKey = key, onBackClick = onBack)
    }
}

@Composable
internal fun CustomRecipeDetailRoute(
    navKey: CustomRecipeNavKey,
    onBackClick: () -> Unit,
) {
    val viewModel: CustomRecipeDetailViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    CustomRecipeDetailScreen(state = state, onBackClick = onBackClick)
}
