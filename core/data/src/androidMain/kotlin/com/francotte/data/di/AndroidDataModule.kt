package com.francotte.data.di

import com.francotte.common.di.IoDispatcherQualifier
import com.francotte.data.favorite.AndroidFavoritesShortcutController
import com.francotte.data.favorite.FavoritesShortcutController
import com.francotte.data.util.ConnectivityManagerNetworkMonitor
import com.francotte.data.util.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific data DI: connectivity and favorites shortcut.
 * The [com.francotte.data.sync.SyncScheduler] implementation is provided by the `:sync` module.
 */
val androidDataModule = module {
    single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext(), get(IoDispatcherQualifier)) }
    single<FavoritesShortcutController> { AndroidFavoritesShortcutController(androidContext()) }
}
