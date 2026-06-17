package com.francotte.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.profile.api.ProfileNavKey
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.profileScreen(onBack: () -> Unit) {
    composable<ProfileNavKey> {
        ProfileRoute(onBackPressed = onBack)
    }
}

@Composable
internal fun ProfileRoute(
    onBackPressed: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ProfileAction.OnBackClick -> onBackPressed()
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
