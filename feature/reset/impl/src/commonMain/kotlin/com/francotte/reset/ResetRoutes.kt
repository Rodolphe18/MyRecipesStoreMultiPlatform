package com.francotte.reset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.reset.api.RequestResetNavKey
import com.francotte.feature.reset.api.ResetPasswordNavKey
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.requestResetScreen(onBack: () -> Unit) {
    composable<RequestResetNavKey> {
        RequestResetPasswordRoute(onBackPressed = onBack)
    }
}

@Composable
internal fun RequestResetPasswordRoute(
    onBackPressed: () -> Unit,
    viewModel: RequestResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RequestResetPasswordScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RequestResetAction.OnBackClick -> onBackPressed()
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}

fun NavGraphBuilder.resetPasswordScreen() {
    composable<ResetPasswordNavKey> { entry ->
        val key = entry.toRoute<ResetPasswordNavKey>()
        ResetPasswordRoute(token = key.token)
    }
}

@Composable
internal fun ResetPasswordRoute(
    token: String,
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ResetPasswordScreen(token = token, state = state, onAction = viewModel::onAction)
}
