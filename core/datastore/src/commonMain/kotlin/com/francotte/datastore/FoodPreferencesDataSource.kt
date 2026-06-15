package com.francotte.datastore

import androidx.datastore.core.DataStore
import com.francotte.datastore.model.PendingFavoriteAction
import com.francotte.datastore.model.StoredConnectionMethod
import com.francotte.datastore.model.User
import com.francotte.datastore.model.UserInfo
import com.francotte.datastore.model.UserPreferences
import com.francotte.model.ConnectionMethod
import com.francotte.model.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import okio.IOException

class FoodPreferencesDataSource(private val userPreferences: DataStore<UserPreferences>) {

    val userData = userPreferences.data
        .map { prefs ->
            UserData(
                userId = prefs.userInfo.user.id,
                userName = prefs.userInfo.user.userName,
                connectionMethod = prefs.toConnectionMethod(),
                email = prefs.userInfo.user.email,
                image = prefs.userInfo.user.image,
                isConnected = prefs.userInfo.connected,
                token = prefs.userInfo.token,
                favoriteRecipesIds = prefs.favoritesIds.keys,
                pendingFavorites = prefs.pendingFavorites.mapValues { it.value.desiredFavorite },
                isPremium = prefs.userInfo.premium,
                launchCount = prefs.launchCount,
                hasRated = prefs.hasRated,
                lastPromptLaunch = prefs.lastPromptLaunch,
            )
        }

    suspend fun setFavoritesIds(favoritesIds: Set<String>) {
        try {
            userPreferences.updateData { prefs ->
                prefs.copy(favoritesIds = favoritesIds.associateWith { true })
            }
        } catch (_: IOException) {
            // Failed to update favorite ids — ignored
        }
    }

    suspend fun setFavoriteId(
        favoriteId: String,
        isFavorite: Boolean,
    ) {
        try {
            userPreferences.updateData { prefs ->
                val updated = if (isFavorite) {
                    prefs.favoritesIds + (favoriteId to true)
                } else {
                    prefs.favoritesIds - favoriteId
                }
                prefs.copy(favoritesIds = updated)
            }
        } catch (_: IOException) {
            // Failed to update user favorites ids — ignored
        }
    }

    suspend fun updateUserInfo(
        isConnected: Boolean,
        name: String,
        userId: Long,
        userToken: String,
        userEmail: String,
        userImage: String,
    ) {
        try {
            userPreferences.updateData { currentPrefs ->
                currentPrefs.copy(
                    userInfo = UserInfo(
                        connected = isConnected,
                        user = User(
                            userName = name,
                            id = userId,
                            email = userEmail,
                            image = userImage,
                        ),
                        token = userToken,
                        premium = currentPrefs.userInfo.premium,
                    ),
                )
            }
        } catch (_: IOException) {
            // Failed to update user info — ignored
        }
    }

    suspend fun deleteFavoriteIds() {
        try {
            userPreferences.updateData { prefs ->
                prefs.copy(favoritesIds = emptyMap())
            }
        } catch (_: IOException) {
            // Failed to update favorite ids — ignored
        }
    }

    suspend fun deleteUser() {
        userPreferences.updateData { prefs ->
            prefs.copy(
                favoritesIds = emptyMap(),
                userInfo = UserInfo(
                    connected = false,
                    user = User(
                        userName = "",
                        id = -1,
                        email = "",
                        image = "",
                    ),
                    token = "",
                ),
            )
        }
    }

    suspend fun isFavoriteLocal(recipeId: String): Boolean =
        userPreferences.data
            .first()
            .favoritesIds
            .containsKey(recipeId)

    suspend fun upsertPendingFavorite(
        recipeId: String,
        desiredFavorite: Boolean,
    ) {
        userPreferences.updateData { prefs ->
            val action = PendingFavoriteAction(
                recipeId = recipeId,
                desiredFavorite = desiredFavorite,
                createdAt = Clock.System.now().toEpochMilliseconds(),
            )
            prefs.copy(pendingFavorites = prefs.pendingFavorites + (recipeId to action))
        }
    }

    suspend fun removePendingFavorite(recipeId: String) {
        userPreferences.updateData { prefs ->
            prefs.copy(pendingFavorites = prefs.pendingFavorites - recipeId)
        }
    }

    suspend fun getPendingFavorites(): List<Pair<String, Boolean>> {
        val prefs = userPreferences.data.first()
        return prefs.pendingFavorites.values.map { it.recipeId to it.desiredFavorite }
    }

    suspend fun clearPendingFavorites() {
        userPreferences.updateData { prefs ->
            prefs.copy(pendingFavorites = emptyMap())
        }
    }

    suspend fun incrementLaunchCount(): Int {
        var newCount = 0
        userPreferences.updateData { prefs ->
            newCount = prefs.launchCount + 1
            prefs.copy(launchCount = newCount)
        }
        return newCount
    }

    suspend fun setHasRated(hasRated: Boolean) {
        userPreferences.updateData { prefs ->
            prefs.copy(hasRated = hasRated)
        }
    }

    suspend fun setLastPromptLaunch(lastPromptLaunch: Int) {
        userPreferences.updateData { prefs ->
            prefs.copy(lastPromptLaunch = lastPromptLaunch)
        }
    }

    suspend fun setPremium(isPremium: Boolean) {
        userPreferences.updateData { currentPrefs ->
            currentPrefs.copy(
                userInfo = currentPrefs.userInfo.copy(premium = isPremium),
            )
        }
    }
}

fun UserPreferences.toConnectionMethod(): ConnectionMethod =
    when (userInfo.user.method) {
        StoredConnectionMethod.EMAIL -> ConnectionMethod.EMAIL
        StoredConnectionMethod.FACEBOOK -> ConnectionMethod.FACEBOOK
        StoredConnectionMethod.GOOGLE -> ConnectionMethod.GOOGLE
    }
