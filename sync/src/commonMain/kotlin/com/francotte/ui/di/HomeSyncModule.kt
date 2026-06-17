package com.francotte.ui.di

import com.francotte.ui.HomeSyncer
import org.koin.dsl.module

/** Shared (Android + iOS) DI for the home sync logic. */
val homeSyncModule = module {
    single { HomeSyncer(get(), get()) }
}
