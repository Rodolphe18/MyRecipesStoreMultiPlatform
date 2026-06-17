package com.francotte.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.auth.SessionRepository
import com.francotte.data.interfaces.UserDataRepository
import com.francotte.domain.RegisterValidator
import com.francotte.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    userDataRepository: UserDataRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    // null = not edited yet → the field mirrors the loaded user's name.
    private val editedName = MutableStateFlow<String?>(null)

    val state: StateFlow<ProfileState> = combine(
        userDataRepository.userData,
        editedName,
    ) { user, name ->
        ProfileState(user = user, editedName = name ?: user.userName)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileState())

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnNameChange -> editedName.value = action.name
            ProfileAction.OnSave -> save()
            ProfileAction.OnBackClick -> Unit
        }
    }

    private fun save() {
        val current = state.value
        val user = current.user ?: return
        val finalName = current.editedName.ifBlank { user.userName }
        viewModelScope.launch {
            // Image upload is deferred (needs the platform image picker → ImageUpload).
            sessionRepository.updateUserInfo(finalName, image = null)
        }
        editedName.value = null
    }
}

data class ProfileState(
    val user: UserData? = null,
    val editedName: String = "",
) {
    val isNameValid: Boolean get() = RegisterValidator.isValidName(editedName)
    val isNameChanged: Boolean get() = user != null && editedName != user.userName
}

sealed interface ProfileAction {
    data class OnNameChange(val name: String) : ProfileAction
    data object OnSave : ProfileAction
    data object OnBackClick : ProfileAction
}
