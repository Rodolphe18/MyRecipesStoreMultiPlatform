package com.francotte.ui.di

import com.francotte.data.sync.SyncScheduler
import com.francotte.ui.WorkManagerSyncScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Provides the real WorkManager-based [SyncScheduler]. */
val syncModule = module {
    single<SyncScheduler> { WorkManagerSyncScheduler(androidContext()) }
}
