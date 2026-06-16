package com.francotte.auth

import com.francotte.auth.strategy.LoginAuthStrategy
import com.francotte.auth.strategy.LoginCredentials
import com.francotte.data.interfaces.UserDataRepository
import com.francotte.database.dao.FullRecipeDao
import com.francotte.network.api.AuthApi
import com.francotte.network.model.ImageUpload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class SessionManager(
    private val loginStrategies: Set<LoginAuthStrategy>,
    private val authSynchronizer: AuthSynchronizer,
    private val preferences: UserDataRepository,
    private val dao: FullRecipeDao,
    private val eventBus: AuthEventBus,
    private val api: AuthApi,
    private val credentialStateClearer: CredentialStateClearer,
    coroutineScope: CoroutineScope,
) : SessionRepository {

    override val isAuthenticated: StateFlow<Boolean> = preferences.userData
        .map { it.isAuthenticated }
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    override val authEvents: SharedFlow<AuthEvent> = eventBus.events

    override suspend fun login(credentials: LoginCredentials): Result<Unit> =
        loginStrategies.firstNotNullOfOrNull { it.tryAuthenticate(credentials) }
            ?: Result.failure(UnsupportedOperationException("No strategy for ${credentials::class.simpleName}"))

    override suspend fun updateUserInfo(userName: String?, image: ImageUpload?) {
        try {
            val token = preferences.userData.first().token
            authSynchronizer.handle(
                api.updateUserProfile(
                    token = "Bearer $token",
                    username = userName,
                    image = image,
                ),
                operation = AuthOperation.UPDATE,
            )
        } catch (e: Exception) {
            // Error while updating user — ignored
        }
    }

    override suspend fun logout() {
        try {
            withContext(NonCancellable) {
                val isConnected = preferences.userData.first().isConnected
                eventBus.emit(AuthEvent.Disconnected(wasConnected = isConnected))
                preferences.updateUserInfo(isConnected = false)
                preferences.deleteFavoriteIds()
                credentialStateClearer.clear()
                dao.deleteAllFavoritesRecipes()
            }
        } catch (e: Exception) {
            // Error while signing out — ignored
        }
    }

    override suspend fun deleteAccount() {
        val userData = preferences.userData.first()
        if (userData.token.isNullOrBlank()) return
        try {
            withContext(NonCancellable) {
                api.deleteUser(userData.userId)
                preferences.deleteUser()
                eventBus.emit(AuthEvent.AccountDeleted)
                credentialStateClearer.clear()
                dao.deleteAllFavoritesRecipes()
            }
        } catch (e: Exception) {
            // Error while deleting account — ignored
        }
    }
}
