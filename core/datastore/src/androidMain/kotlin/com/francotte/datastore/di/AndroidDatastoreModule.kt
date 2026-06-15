package com.francotte.datastore.di

import androidx.datastore.core.DataStore
import com.francotte.datastore.createUserPreferencesDataStore
import com.francotte.datastore.migration.SharedPrefsToDataStoreMigration
import com.francotte.datastore.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

/** Android datastore DI: provides the typed [DataStore] backed by a JSON file in filesDir. */
val androidDatastoreModule = module {
    single<DataStore<UserPreferences>> {
        val context = androidContext()
        createUserPreferencesDataStore(
            producePath = { File(context.filesDir, "datastore/user_preferences.json").absolutePath },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            migrations = listOf(SharedPrefsToDataStoreMigration(context)),
        )
    }
}
