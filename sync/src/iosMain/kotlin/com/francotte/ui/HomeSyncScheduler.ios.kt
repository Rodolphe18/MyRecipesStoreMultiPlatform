package com.francotte.ui

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import platform.BackgroundTasks.BGProcessingTaskRequest
import platform.BackgroundTasks.BGTask
import platform.BackgroundTasks.BGTaskScheduler

/**
 * iOS counterpart of the Android WorkManager `HomeSyncScheduler`.
 *
 * iOS has no WorkManager. The equivalent is the **BackgroundTasks** framework (`BGTaskScheduler`):
 *  - [runNow] launches [HomeSyncer.sync] immediately on app launch (foreground, user-visible) —
 *    the reliable path, mirroring "populate the home on startup".
 *  - [register] + [schedule] submit a deferred `BGProcessingTaskRequest` that iOS runs
 *    opportunistically when on network. Unlike WorkManager this is best-effort, not guaranteed.
 *
 * Swift wiring required (cannot be done from Kotlin):
 *  1. Info.plist → `BGTaskSchedulerPermittedIdentifiers` must contain [TASK_ID].
 *  2. In `application(_:didFinishLaunchingWithOptions:)`, call `HomeSyncScheduler().register()`
 *     BEFORE launch returns, then `runNow()` and/or `schedule()`.
 */
class HomeSyncScheduler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val homeSyncer: HomeSyncer
        get() = KoinPlatform.getKoin().get()

    /** Immediate foreground sync — call at launch so the home populates without delay. */
    fun runNow() {
        scope.launch { runCatching { homeSyncer.sync() } }
    }

    /** Register the background-task handler. Must be called during `didFinishLaunching`. */
    @OptIn(ExperimentalForeignApi::class)
    fun register() {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            identifier = TASK_ID,
            usingQueue = null,
        ) { task ->
            handle(task as BGTask)
        }
    }

    /** Submit a deferred, network-constrained processing task. */
    @OptIn(ExperimentalForeignApi::class)
    fun schedule() {
        val request = BGProcessingTaskRequest(TASK_ID).apply {
            requiresNetworkConnectivity = true
            requiresExternalPower = false
        }
        runCatching { BGTaskScheduler.sharedScheduler.submitTaskRequest(request, error = null) }
    }

    private fun handle(task: BGTask) {
        val job: Job = scope.launch {
            runCatching { homeSyncer.sync() }
            task.setTaskCompletedWithSuccess(true)
        }
        task.expirationHandler = {
            job.cancel()
            task.setTaskCompletedWithSuccess(false)
        }
    }

    companion object {
        const val TASK_ID = "com.francotte.home-sync"
    }
}
