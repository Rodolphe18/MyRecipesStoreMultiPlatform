package com.francotte.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.francotte.datastore.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import okio.FileSystem
import okio.Path.Companion.toPath

/**
 * Builds the typed [DataStore] for [UserPreferences]. The actual file path and any
 * platform-specific [migrations] are supplied by the platform module.
 */
fun createUserPreferencesDataStore(
    producePath: () -> String,
    scope: CoroutineScope,
    migrations: List<DataMigration<UserPreferences>> = emptyList(),
    fileSystem: FileSystem = FileSystem.SYSTEM,
): DataStore<UserPreferences> =
    DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = fileSystem,
            serializer = UserPreferencesSerializer,
            producePath = { producePath().toPath() },
        ),
        migrations = migrations,
        scope = scope,
    )
