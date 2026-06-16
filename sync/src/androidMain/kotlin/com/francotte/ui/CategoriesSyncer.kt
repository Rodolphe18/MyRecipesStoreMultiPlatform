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
import com.francotte.data.interfaces.CategoriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class CategoriesSyncWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params), KoinComponent {

    private val repository: CategoriesRepository by inject()

    override suspend fun doWork(): Result =
        try {
            repository.refreshAllMealCategories(false)
            Result.success()
        } catch (t: Throwable) {
            val maxAttempts = 3
            if (runAttemptCount + 1 >= maxAttempts) Result.failure() else Result.retry()
        }
}

object CategoriesSyncScheduler {
    private const val UNIQUE_CATEGORIES_ONE_SHOT = "categories-sync-once"

    fun enqueueOneShot(context: Context) {
        val appCtx = context.applicationContext
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<CategoriesSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30_000, TimeUnit.MILLISECONDS)
            .addTag("search-sync")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            WorkManager.getInstance(appCtx).enqueueUniqueWork(
                UNIQUE_CATEGORIES_ONE_SHOT,
                ExistingWorkPolicy.REPLACE,
                request,
            )
        }
    }
}
