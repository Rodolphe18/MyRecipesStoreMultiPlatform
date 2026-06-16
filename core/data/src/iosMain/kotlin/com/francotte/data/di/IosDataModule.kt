package com.francotte.data.di

import com.francotte.data.favorite.FavoritesShortcutController
import com.francotte.data.sync.SyncScheduler
import com.francotte.data.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

/**
 * iOS-specific data DI. These are minimal implementations for now:
 * - [NetworkMonitor] reports always-online (replace with NWPathMonitor later).
 * - The favorites shortcut and background sync have no iOS equivalent yet (no-op).
 */
val iosDataModule = module {
    single<NetworkMonitor> { IosNetworkMonitor() }
    single<FavoritesShortcutController> { NoOpFavoritesShortcutController() }
    single<SyncScheduler> { NoOpSyncScheduler() }
}

private class IosNetworkMonitor : NetworkMonitor {
    override val isOnline: Flow<Boolean> = flowOf(true)
}

private class NoOpFavoritesShortcutController : FavoritesShortcutController {
    override fun setEnabled(enabled: Boolean) = Unit
}

private class NoOpSyncScheduler : SyncScheduler {
    override fun enqueueForLogin() = Unit
    override fun enqueueForToggle() = Unit
}
