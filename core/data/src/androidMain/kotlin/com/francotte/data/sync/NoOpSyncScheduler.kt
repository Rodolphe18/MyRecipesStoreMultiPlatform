package com.francotte.data.sync

/**
 * Temporary no-op scheduler. The real WorkManager-based implementation lands with the
 * `:sync` module migration; until then favorite toggles are persisted locally without
 * a background sync being enqueued.
 */
class NoOpSyncScheduler : SyncScheduler {
    override fun enqueueForLogin() = Unit
    override fun enqueueForToggle() = Unit
}
