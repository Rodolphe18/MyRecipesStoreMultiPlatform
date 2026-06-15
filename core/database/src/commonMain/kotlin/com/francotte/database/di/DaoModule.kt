package com.francotte.database.di

import com.francotte.database.FoodDatabase
import org.koin.dsl.module

/**
 * Common DAO DI. The [FoodDatabase] instance itself is provided by the platform-specific
 * module (see `androidDatabaseModule`) because building it requires platform context.
 */
val daoModule = module {
    single { get<FoodDatabase>().lightRecipeDao() }
    single { get<FoodDatabase>().fullRecipeDao() }
    single { get<FoodDatabase>().lightCategoryDao() }
    single { get<FoodDatabase>().fullCategoryDao() }
    single { get<FoodDatabase>().ingredientsDao() }
    single { get<FoodDatabase>().areasDao() }
    single { get<FoodDatabase>().areaFtsDao() }
    single { get<FoodDatabase>().categoryFtsDao() }
    single { get<FoodDatabase>().ingredientFtsDao() }
    single { get<FoodDatabase>().recipeFtsDao() }
    single { get<FoodDatabase>().searchIndexStateDao() }
}
