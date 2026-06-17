package com.francotte.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.data.interfaces.UserHomeRepository
import com.francotte.model.LikeableRecipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SECTION_LOAD_ERROR = "An error occurred while loading the section"

class SectionViewModel(
    private val repository: UserHomeRepository,
    private val sectionName: String,
) : ViewModel() {

    private val _state = MutableStateFlow(SectionState(title = sectionName))
    val state = _state.asStateFlow()

    private val _events = Channel<SectionEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeSection()
    }

    fun onAction(action: SectionAction) {
        when (action) {
            SectionAction.OnReload -> refresh()
            SectionAction.OnBackClick -> viewModelScope.launch {
                _events.send(SectionEvent.NavigateBack)
            }
            is SectionAction.OnRecipeClick -> {
                val recipes = state.value.recipes
                viewModelScope.launch {
                    _events.send(
                        SectionEvent.NavigateToRecipe(
                            ids = recipes.map { it.recipe.idMeal },
                            index = action.index,
                            title = recipes[action.index].recipe.strMeal,
                        )
                    )
                }
            }
            // Favorite toggling is handled outside the VM (FavoriteManager decoupling).
            is SectionAction.OnToggleFavorite -> Unit
        }
    }

    /**
     * Section data is pre-fetched at startup, so the DB is already populated on entry: we only
     * observe it. The collector ends loading on the first emission and flags an error when empty;
     * `catch` covers a DB read failure so the screen doesn't hang on the spinner.
     */
    private fun observeSection() {
        repository.observeFoodAreaSection(sectionName)
            .onEach { recipes ->
                _state.update {
                    it.copy(
                        recipes = recipes,
                        isLoading = false,
                        error = if (recipes.isEmpty()) SECTION_LOAD_ERROR else null,
                    )
                }
            }
            .catch {
                _state.update { it.copy(isLoading = false, error = SECTION_LOAD_ERROR) }
            }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val errorMessage = repository.refreshSpecificFoodAreaSection(sectionName, true)
            if (errorMessage != null) {
                _events.send(SectionEvent.ShowSnackbar(errorMessage))
            }
            _state.update { it.copy(isRefreshing = false) }
        }
    }
}

data class SectionState(
    val title: String = "",
    val recipes: List<LikeableRecipe> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

sealed interface SectionAction {
    data object OnReload : SectionAction
    data object OnBackClick : SectionAction
    data class OnRecipeClick(val index: Int) : SectionAction
    data class OnToggleFavorite(val recipe: LikeableRecipe) : SectionAction
}

sealed interface SectionEvent {
    data class NavigateToRecipe(val ids: List<String>, val index: Int, val title: String) : SectionEvent
    data object NavigateBack : SectionEvent
    data class ShowSnackbar(val message: String) : SectionEvent
}
