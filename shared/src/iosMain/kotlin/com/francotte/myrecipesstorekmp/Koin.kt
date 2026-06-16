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
        )
    }
}
