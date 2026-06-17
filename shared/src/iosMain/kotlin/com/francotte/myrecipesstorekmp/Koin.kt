package com.francotte.myrecipesstorekmp

import com.francotte.auth.di.authModule
import com.francotte.auth.di.iosAuthModule
import com.francotte.common.di.coroutinesModule
import com.francotte.data.di.dataModule
import com.francotte.data.di.iosDataModule
import com.francotte.database.di.daoModule
import com.francotte.database.di.iosDatabaseModule
import com.francotte.datastore.di.datastoreModule
import com.francotte.datastore.di.iosDatastoreModule
import com.francotte.domain.di.domainModule
import com.francotte.network.di.iosNetworkModule
import com.francotte.network.di.networkModule
import com.francotte.ui.HomeSyncScheduler
import com.francotte.ui.di.homeSyncModule
import org.koin.core.context.startKoin

/**
 * iOS entry point: starts Koin with the shared foundation graph.
 * Common modules + the iOS-specific platform modules (Darwin HTTP client, native SQLite driver,
 * Documents-directory DataStore, no-op shortcut/sync, no-op credential clearer).
 *
 * Call once from Swift at app launch: `KoinKt.doInitKoin()`.
 */
fun initKoin() {
    startKoin {
        modules(
            coroutinesModule,
            networkModule,
            iosNetworkModule,
            daoModule,
            iosDatabaseModule,
            datastoreModule,
            iosDatastoreModule,
            dataModule,
            iosDataModule,
            domainModule,
            authModule,
            iosAuthModule,
            homeSyncModule,
        )
    }
}

private val homeSyncScheduler = HomeSyncScheduler()

/**
 * Home sync triggers exposed to Swift (the `shared` framework's public surface).
 *
 * Recommended Swift wiring in `application(_:didFinishLaunchingWithOptions:)`:
 * ```swift
 * KoinKt.doInitKoin()
 * KoinKt.registerHomeSync()   // must run before launch returns
 * KoinKt.runHomeSyncNow()     // immediate foreground populate
 * KoinKt.scheduleHomeSync()   // opportunistic background refresh
 * ```
 * Also add [HomeSyncScheduler.TASK_ID] to `BGTaskSchedulerPermittedIdentifiers` in Info.plist.
 */
fun registerHomeSync() = homeSyncScheduler.register()

fun runHomeSyncNow() = homeSyncScheduler.runNow()

fun scheduleHomeSync() = homeSyncScheduler.schedule()
