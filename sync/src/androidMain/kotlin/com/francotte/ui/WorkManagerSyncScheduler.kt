package com.francotte.ui

import android.content.Context
import com.francotte.data.sync.SyncScheduler

/** WorkManager-backed [SyncScheduler] (replaces the temporary NoOpSyncScheduler). */
class WorkManagerSyncScheduler(private val context: Context) : SyncScheduler {
    override fun enqueueForLogin() = FavoritesSyncScheduler.enqueueForLogin(context)
    override fun enqueueForToggle() = FavoritesSyncScheduler.enqueueForToggle(context)
}
