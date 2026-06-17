package com.francotte.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.register.api.RegisterNavKey
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.registerScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
) {
    composable<RegisterNavKey> {
        RegisterRoute(onBackPressed = onBack, navigateToFavoriteScreen = onRegistered)
    }
}

@Composable
internal fun RegisterRoute(
    onBackPressed: () -> Unit,
    navigateToFavoriteScreen: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.NavigateToFavorites -> navigateToFavoriteScreen()
                is RegisterEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick -> onBackPressed()
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
