package com.francotte.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.francotte.database.sql.FoodDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android database DI: provides the SQLDelight [SqlDriver] backed by Android SQLite. */
val androidDatabaseModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = FoodDb.Schema,
            context = androidContext(),
            name = "food.db",
        )
    }
}
