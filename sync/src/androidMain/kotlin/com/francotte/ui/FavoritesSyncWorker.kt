package com.francotte.ui

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.francotte.data.mapper.dto.asEntity
import com.francotte.database.dao.FullRecipeDao
import com.francotte.datastore.FoodPreferencesDataSource
import com.francotte.network.api.FavoriteApi
import com.francotte.network.api.RecipeApi
import com.francotte.network.model.NetworkRecipe
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.util.concurrent.TimeUnit

class FavoritesSyncWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params), KoinComponent {

    private val api: FavoriteApi by inject()
    private val repo: FoodPreferencesDataSource by inject()
    private val recipeApi: RecipeApi by inject()
    private val dao: FullRecipeDao by inject()

    override suspend fun doWork(): Result {
        val user = repo.userData.first()
        val token = user.token

        val reason = inputData.getString(KEY_REASON) ?: REASON_TOGGLE
        val isReasonLogin = reason == REASON_LOGIN

        if (!user.isConnected || token.isNullOrBlank()) {
            return Result.failure()
        }

        val pending = repo.getPendingFavorites()

        if (pending.isEmpty()) return Result.success()

        val maxAttemptsBeforeReconcile = 3
        if (runAttemptCount >= maxAttemptsBeforeReconcile) {
            return reconcileFromServer(token)
        }

        try {
            for ((recipeId, desiredFav) in pending) {
                when (trySyncOne(token, recipeId, desiredFav)) {
                    ItemSyncResult.Synced -> Unit
                    ItemSyncResult.StopNoLogin -> return Result.failure()
                    ItemSyncResult.Reconcile -> return reconcileFromServer(token)
                    ItemSyncResult.Retry -> return Result.retry()
                }
            }
            if (isReasonLogin) {
                val prefetchResult = prefetchMissingFavoriteRecipes()
                if (!prefetchResult) return Result.retry()
            }

            return Result.success()
        } catch (_: Exception) {
            return Result.retry()
        }
    }

    private suspend fun trySyncOne(
        token: String,
        recipeId: String,
        desiredFav: Boolean,
    ): ItemSyncResult =
        try {
            if (desiredFav) {
                api.addFavorite(recipeId, "Bearer $token")
            } else {
                api.removeFavorite(recipeId, "Bearer $token")
            }
            repo.removePendingFavorite(recipeId)
            ItemSyncResult.Synced
        } catch (e: ResponseException) {
            when (e.response.status.value) {
                401, 403 -> ItemSyncResult.StopNoLogin
                in 400..499 -> ItemSyncResult.Reconcile
                else -> ItemSyncResult.Retry
            }
        } catch (_: IOException) {
            ItemSyncResult.Retry
        }

    private suspend fun reconcileFromServer(token: String): Result =
        try {
            val serverIds = api.getFavoriteRecipeIds("Bearer $token")
            repo.setFavoritesIds(serverIds.toSet())
            repo.clearPendingFavorites()
            Result.success()
        } catch (e: ResponseException) {
            when (e.response.status.value) {
                401, 403 -> Result.failure()
                else -> Result.retry()
            }
        } catch (_: IOException) {
            Result.retry()
        }

    private suspend fun prefetchMissingFavoriteRecipes(): Boolean {
        val ids = repo.userData.first().favoriteRecipesIds.distinct()
        if (ids.isEmpty()) return true

        val existing = dao.getExistingIds(ids).toSet()
        val missing = ids.filterNot(existing::contains)
        if (missing.isEmpty()) return true

        return try {
            for (idStr in missing) {
                val id = idStr.toLongOrNull() ?: continue
                val network = recipeApi.getMealDetail(id)
                    .meals
                    .filterIsInstance<NetworkRecipe>()
                    .firstOrNull() ?: continue

                dao.insertFullRecipe(network.asEntity())
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    private enum class ItemSyncResult { Synced, StopNoLogin, Reconcile, Retry }
}

object FavoritesSyncScheduler {

    fun enqueueForToggle(context: Context) {
        enqueue(context, REASON_TOGGLE, ExistingWorkPolicy.KEEP)
    }

    fun enqueueForLogin(context: Context) {
        enqueue(context, REASON_LOGIN, ExistingWorkPolicy.REPLACE)
    }

    private fun enqueue(context: Context, reason: String, policy: ExistingWorkPolicy) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<FavoritesSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30_000, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(KEY_REASON to reason))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork("favorites-sync", policy, request)
    }
}

private const val KEY_REASON = "reason"
private const val REASON_TOGGLE = "toggle"
private const val REASON_LOGIN = "login"
