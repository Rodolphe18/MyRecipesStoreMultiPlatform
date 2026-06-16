package com.francotte.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.francotte.database.sql.FoodDb
import org.koin.dsl.module

/** iOS database DI: provides the SQLDelight [SqlDriver] backed by the native SQLite driver. */
val iosDatabaseModule = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            schema = FoodDb.Schema,
            name = "food.db",
        )
    }
}
