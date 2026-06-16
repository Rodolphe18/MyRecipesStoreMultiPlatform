package com.francotte.datastore.di

import androidx.datastore.core.DataStore
import com.francotte.datastore.createUserPreferencesDataStore
import com.francotte.datastore.model.UserPreferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/** iOS datastore DI: stores the JSON preferences file in the app's Documents directory. */
val iosDatastoreModule = module {
    single<DataStore<UserPreferences>> {
        createUserPreferencesDataStore(
            producePath = { "${iosDocumentsDirectory()}/user_preferences.json" },
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun iosDocumentsDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
