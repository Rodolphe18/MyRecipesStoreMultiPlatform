package com.francotte.myrecipesstorekmp.android

import android.app.Application
import com.francotte.myrecipesstorekmp.android.di.appModules
import com.francotte.ui.HomeSyncScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyRecipesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyRecipesApplication)
            modules(appModules)
        }
        // One-shot home population on startup (network-constrained WorkManager job),
        // mirroring the native app. The worker resolves repositories from Koin, so it
        // must be enqueued after startKoin.
        HomeSyncScheduler.enqueueOneShot(this)
    }
}
