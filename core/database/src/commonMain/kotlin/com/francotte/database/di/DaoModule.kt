package com.francotte.database.di

import com.francotte.database.dao.AreaDao
import com.francotte.database.dao.FullCategoryDao
import com.francotte.database.dao.FullRecipeDao
import com.francotte.database.dao.IngredientDao
import com.francotte.database.dao.LightCategoryDao
import com.francotte.database.dao.LightRecipeDao
import com.francotte.database.dao.SearchIndexStateDao
import com.francotte.database.sql.FoodDb
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Common database DI: builds the SQLDelight [FoodDb] from the platform [app.cash.sqldelight.db.SqlDriver]
 * (provided by `androidDatabaseModule` / `iosDatabaseModule`) and exposes the DAOs.
 * DAOs run their queries on the IO dispatcher (qualifier "IoDispatcher", provided by core:common).
 */
private val ioDispatcher = named("IoDispatcher")

val daoModule = module {
    single { FoodDb(get()) }

    single { LightRecipeDao(get(), get(ioDispatcher)) }
    single { FullRecipeDao(get(), get(ioDispatcher)) }
    single { LightCategoryDao(get(), get(ioDispatcher)) }
    single { FullCategoryDao(get(), get(ioDispatcher)) }
    single { IngredientDao(get(), get(ioDispatcher)) }
    single { AreaDao(get(), get(ioDispatcher)) }
    single { SearchIndexStateDao(get(), get(ioDispatcher)) }
}
