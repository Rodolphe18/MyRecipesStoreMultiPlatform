package com.francotte.data.di

import com.francotte.common.di.IoDispatcherQualifier
import com.francotte.data.favorite.AndroidFavoritesShortcutController
import com.francotte.data.favorite.FavoritesShortcutController
import com.francotte.data.sync.NoOpSyncScheduler
import com.francotte.data.sync.SyncScheduler
import com.francotte.data.util.ConnectivityManagerNetworkMonitor
import com.francotte.data.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android-specific data DI: connectivity, favorites shortcut and (temporary) sync scheduler. */
val androidDataModule = module {
    single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext(), get(IoDispatcherQualifier)) }
    single<FavoritesShortcutController> { AndroidFavoritesShortcutController(androidContext()) }
    single<SyncScheduler> { NoOpSyncScheduler() }
}
