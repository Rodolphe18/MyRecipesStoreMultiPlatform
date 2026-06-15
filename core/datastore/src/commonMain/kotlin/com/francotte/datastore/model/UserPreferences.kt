package com.francotte.datastore.model

import kotlinx.serialization.Serializable

/**
 * Multiplatform replacement for the former Proto DataStore `UserPreferences` message.
 * Persisted as JSON via kotlinx-serialization.
 */
@Serializable
data class UserPreferences(
    val favoritesIds: Map<String, Boolean> = emptyMap(),
    val userInfo: UserInfo = UserInfo(),
    val pendingFavorites: Map<String, PendingFavoriteAction> = emptyMap(),
    val launchCount: Int = 0,
    val hasRated: Boolean = false,
    val lastPromptLaunch: Int = 0,
)

@Serializable
data class UserInfo(
    val connected: Boolean = false,
    val user: User = User(),
    val token: String = "",
    val premium: Boolean = false,
)

@Serializable
data class User(
    val id: Long = 0L,
    val userName: String = "",
    val method: StoredConnectionMethod = StoredConnectionMethod.EMAIL,
    val email: String = "",
    val image: String = "",
)

@Serializable
enum class StoredConnectionMethod {
    EMAIL,
    FACEBOOK,
    GOOGLE,
}

@Serializable
data class PendingFavoriteAction(
    val recipeId: String = "",
    val desiredFavorite: Boolean = false,
    val createdAt: Long = 0L,
)
