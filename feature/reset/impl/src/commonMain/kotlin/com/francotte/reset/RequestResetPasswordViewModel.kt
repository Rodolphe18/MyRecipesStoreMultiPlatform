package com.francotte.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.auth.PasswordResetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RequestResetPasswordViewModel(
    private val passwordResetRepository: PasswordResetRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RequestResetState())
    val state = _state.asStateFlow()

    fun onAction(action: RequestResetAction) {
        when (action) {
            is RequestResetAction.OnSendClick -> requestReset(action.email)
            RequestResetAction.OnBackClick -> Unit
        }
    }

    private fun requestReset(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isSuccess = false, errorMessage = null) }
            val result = passwordResetRepository.requestPasswordReset(email)
            _state.update {
                it.copy(
                    isLoading = false,
                    isSuccess = result.isSuccess,
                    errorMessage = if (result.isFailure) "An error occured. Try again" else null,
                )
            }
        }
    }
}

data class RequestResetState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface RequestResetAction {
    data class OnSendClick(val email: String) : RequestResetAction
    data object OnBackClick : RequestResetAction
}
