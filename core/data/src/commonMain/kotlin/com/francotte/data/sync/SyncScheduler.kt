package com.francotte.data.sync

/**
 * Schedules background favorite synchronisation. The platform implementation
 * (e.g. WorkManager on Android) is provided by the `:sync` module.
 */
interface SyncScheduler {
    fun enqueueForLogin()
    fun enqueueForToggle()
}
