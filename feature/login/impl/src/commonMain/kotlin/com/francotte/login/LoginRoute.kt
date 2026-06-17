package com.francotte.login

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.login.api.LoginNavKey
import com.francotte.ui.LocalSnackbarHostState
import org.koin.compose.viewmodel.koinViewModel

/**
 * @param onGoogleSignIn provided by the platform: obtains a Google ID token (Android: CredentialManager)
 * and invokes the callback with it; the ViewModel then completes the session login.
 */
fun NavGraphBuilder.loginScreen(
    onRegister: () -> Unit,
    onResetPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleSignIn: (onIdToken: (String) -> Unit) -> Unit,
) {
    composable<LoginNavKey> {
        LoginRoute(
            onRegister = onRegister,
            onResetPassword = onResetPassword,
            onLoginSuccess = onLoginSuccess,
            onGoogleSignIn = onGoogleSignIn,
        )
    }
}

@Composable
internal fun LoginRoute(
    onRegister: () -> Unit,
    onResetPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleSignIn: (onIdToken: (String) -> Unit) -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToFavorites -> onLoginSuccess()
                is LoginEvent.ShowSnackbar -> snackbarHostState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnGoogleLoginClick -> onGoogleSignIn { idToken -> viewModel.loginWithGoogle(idToken) }
                LoginAction.OnRegisterClick -> onRegister()
                LoginAction.OnResetPasswordClick -> onResetPassword()
                else -> viewModel.onAction(action)
            }
        },
    )
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
