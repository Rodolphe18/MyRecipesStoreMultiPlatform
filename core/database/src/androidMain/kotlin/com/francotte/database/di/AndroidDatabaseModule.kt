package com.francotte.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.francotte.database.FoodDatabase
import com.francotte.database.MIGRATION_2_3
import com.francotte.database.MIGRATION_3_4
import com.francotte.database.MIGRATION_4_5
import com.francotte.database.MIGRATION_5_6
import com.francotte.database.MIGRATION_6_7
import com.francotte.database.MIGRATION_7_8
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android database DI: builds the Room [FoodDatabase] with the bundled SQLite driver. */
val androidDatabaseModule = module {
    single<FoodDatabase> {
        Room
            .databaseBuilder(
                androidContext(),
                FoodDatabase::class.java,
                "food-database",
            )
            .addMigrations(
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
            )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
