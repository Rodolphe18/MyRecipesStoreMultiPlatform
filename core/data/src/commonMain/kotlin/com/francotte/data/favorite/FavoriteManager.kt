package com.francotte.data.favorite

import com.francotte.data.interfaces.FavoriteHelper
import com.francotte.data.interfaces.UserDataRepository
import com.francotte.data.sync.SyncScheduler
import com.francotte.data.util.NetworkMonitor
import com.francotte.model.LikeableRecipe
import com.francotte.network.api.FavoriteApi
import com.francotte.network.model.ImageUpload
import com.francotte.network.model.NetworkCustomIngredient
import com.francotte.network.model.NetworkCustomRecipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

sealed interface ToggleFavoriteResult {
    data class Success(val added: Boolean) : ToggleFavoriteResult
    data object Offline : ToggleFavoriteResult
    data object Unauthenticated : ToggleFavoriteResult
}

class FavoriteManager(
    private val coroutineScope: CoroutineScope,
    private val api: FavoriteApi,
    private val networkMonitor: NetworkMonitor,
    private val foodPreferencesDataSource: UserDataRepository,
    private val syncScheduler: SyncScheduler,
    private val favoritesShortcutController: FavoritesShortcutController,
) : FavoriteHelper {

    init {
        coroutineScope.launch {
            foodPreferencesDataSource.userData
                .map { it.isAuthenticated }
                .collect { isAuthenticated -> favoritesShortcutController.setEnabled(isAuthenticated) }
        }
    }

    override suspend fun toggleRecipeFavorite(likeableRecipe: LikeableRecipe): ToggleFavoriteResult {
        val userData = foodPreferencesDataSource.userData.first()
        if (!userData.isAuthenticated) return ToggleFavoriteResult.Unauthenticated

        val recipeId = likeableRecipe.recipe.idMeal
        val currentlyFavorite = foodPreferencesDataSource.userData.first().favoriteRecipesIds.contains(recipeId)
        val desiredFavorite = !currentlyFavorite
        foodPreferencesDataSource.setFavoriteId(recipeId, desiredFavorite)
        foodPreferencesDataSource.upsertPendingFavorite(recipeId, desiredFavorite)
        syncScheduler.enqueueForToggle()

        val online = networkMonitor.isOnline.first()
        return if (online) ToggleFavoriteResult.Success(added = desiredFavorite)
        else ToggleFavoriteResult.Offline
    }

    suspend fun createRecipe(
        title: String,
        ingredients: List<NetworkCustomIngredient>,
        instructions: String,
        image: ImageUpload?,
    ): Result<Unit> {
        val token = foodPreferencesDataSource.userData.first().token
        val ingredientsJson = json.encodeToString(ListSerializer(NetworkCustomIngredient.serializer()), ingredients)
        return try {
            api.addRecipe("Bearer $token", image, title, instructions, ingredientsJson)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRecipe(
        recipeId: String,
        title: String,
        ingredients: List<NetworkCustomIngredient>,
        instructions: String,
        image: ImageUpload?,
    ): Result<Unit> {
        val token = foodPreferencesDataSource.userData.first().token
        val ingredientsJson = json.encodeToString(ListSerializer(NetworkCustomIngredient.serializer()), ingredients)
        return try {
            api.updateRecipe("Bearer $token", recipeId, image, title, instructions, ingredientsJson)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserRecipes(): List<NetworkCustomRecipe> {
        val token = foodPreferencesDataSource.userData.first().token
        return if (!token.isNullOrBlank()) api.getUserRecipes("Bearer $token")
        else emptyList()
    }

    suspend fun getUserCustomRecipe(customRecipeId: String): NetworkCustomRecipe {
        val token = foodPreferencesDataSource.userData.first().token
        return if (!token.isNullOrBlank()) api.getUserRecipe("Bearer $token", customRecipeId)
        else throw Exception("Not authenticated")
    }

    private companion object {
        val json = Json
    }
}
