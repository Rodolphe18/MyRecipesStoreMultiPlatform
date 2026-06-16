package com.francotte.myrecipesstorekmp.android

import android.app.Application
import com.francotte.myrecipesstorekmp.android.di.appModules
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
    }
}
