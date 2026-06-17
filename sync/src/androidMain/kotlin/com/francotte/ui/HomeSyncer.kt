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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class HomeSyncWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params), KoinComponent {

    private val homeSyncer: HomeSyncer by inject()

    override suspend fun doWork(): Result =
        try {
            homeSyncer.sync()
            Result.success()
        } catch (t: Throwable) {
            val maxAttempts = 3
            if (runAttemptCount + 1 >= maxAttempts) Result.failure() else Result.retry()
        }
}

object HomeSyncScheduler {
    private const val UNIQUE_ONE_SHOT = "home-sync-once"

    fun enqueueOneShot(context: Context) {
        val appCtx = context.applicationContext
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<HomeSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30_000, TimeUnit.MILLISECONDS)
            .addTag("home-sync")
            .build()

        WorkManager.getInstance(appCtx).enqueueUniqueWork(
            UNIQUE_ONE_SHOT,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }
}
