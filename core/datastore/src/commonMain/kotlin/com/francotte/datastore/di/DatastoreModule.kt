package com.francotte.datastore.di

import com.francotte.datastore.FoodPreferencesDataSource
import org.koin.dsl.module

/**
 * Common datastore DI. The [androidx.datastore.core.DataStore] itself is provided by the
 * platform-specific module (see `androidDatastoreModule`) because its file path and
 * migrations are platform dependent.
 */
val datastoreModule = module {
    single { FoodPreferencesDataSource(get()) }
}
