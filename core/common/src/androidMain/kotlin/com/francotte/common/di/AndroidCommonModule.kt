package com.francotte.common.di

import androidx.work.Configuration
import coil.ImageLoader
import coil.request.CachePolicy
import kotlinx.coroutines.asCoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Koin replacement for the former Hilt `SharedExecutorModule`.
 * Provides the shared executor, the Coil [ImageLoader] and the WorkManager [Configuration].
 */
val androidCommonModule = module {
    single<ExecutorService> { Executors.newFixedThreadPool(4) }

    single {
        ImageLoader
            .Builder(androidContext())
            .crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .dispatcher(get<ExecutorService>().asCoroutineDispatcher())
            .build()
    }

    single {
        Configuration
            .Builder()
            .setExecutor(get<ExecutorService>())
            .build()
    }
}
