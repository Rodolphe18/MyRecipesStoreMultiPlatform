package com.francotte.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

/**
 * Koin replacement for the former Hilt `DispatchersModule` / `CoroutineScopesModule`.
 * Provides the IO and Default dispatchers plus an application-wide [CoroutineScope].
 */
val coroutinesModule = module {
    single<CoroutineDispatcher>(IoDispatcherQualifier) { ioDispatcher }
    single<CoroutineDispatcher>(DefaultDispatcherQualifier) { Dispatchers.Default }
    single(ApplicationScopeQualifier) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(IoDispatcherQualifier))
    }
}
