package com.francotte.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.auth.SessionRepository
import com.francotte.auth.strategy.EmailPasswordCredentials
import com.francotte.auth.strategy.GoogleCredentials
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLoginClick -> loginWithMailAndPassword(action.usernameOrMail, action.password)
            // Google login (idToken obtained on the platform side) and pure navigation are
            // handled by LoginRoute.
            LoginAction.OnGoogleLoginClick,
            LoginAction.OnRegisterClick,
            LoginAction.OnResetPasswordClick -> Unit
        }
    }

    /** Completes Google sign-in once the platform layer has obtained an [idToken]. */
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = runCatching { sessionRepository.login(GoogleCredentials(idToken)) }
                .getOrElse { Result.failure(it) }
            if (result.isSuccess) onSuccess() else onError()
        }
    }

    private fun loginWithMailAndPassword(userNameOrMail: String, password: String) {
        if (state.value.isLoading) return
        if (userNameOrMail.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = runCatching { sessionRepository.login(EmailPasswordCredentials(userNameOrMail, password)) }
                .getOrElse { Result.failure(it) }
            if (result.isSuccess) onSuccess() else onError()
        }
    }

    private suspend fun onSuccess() {
        _state.update { it.copy(isLoading = false) }
        _events.send(LoginEvent.NavigateToFavorites)
    }

    private suspend fun onError() {
        _state.update { it.copy(isLoading = false) }
        _events.send(LoginEvent.ShowSnackbar("Sign-in failed"))
    }
}

data class LoginState(
    val isLoading: Boolean = false,
)

sealed interface LoginAction {
    data class OnLoginClick(val usernameOrMail: String, val password: String) : LoginAction
    data object OnGoogleLoginClick : LoginAction
    data object OnRegisterClick : LoginAction
    data object OnResetPasswordClick : LoginAction
}

sealed interface LoginEvent {
    data object NavigateToFavorites : LoginEvent
    data class ShowSnackbar(val message: String) : LoginEvent
}
