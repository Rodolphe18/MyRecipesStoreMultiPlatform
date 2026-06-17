package com.francotte.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.data.interfaces.FavoritesRepository
import com.francotte.model.CustomRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * Read-only view of a user custom recipe. Editing + image upload (Android photo/camera picker)
 * is deferred to a dedicated pass that adds an `expect/actual` image-picker producing [ImageUpload].
 */
class CustomRecipeDetailViewModel(
    private val recipeId: String?,
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CustomRecipeDetailState())
    val state = _state.asStateFlow()

    init {
        recipeId?.let { id ->
            favoritesRepository.observeUserCustomRecipe(id)
                .onEach { result -> _state.update { it.copy(recipe = result.getOrNull()) } }
                .launchIn(viewModelScope)
        }
    }
}

data class CustomRecipeDetailState(
    val recipe: CustomRecipe? = null,
)
