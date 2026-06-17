package com.francotte.favorites.di

import com.francotte.favorites.CustomRecipeDetailViewModel
import com.francotte.favorites.FavoritesViewModel
import com.francotte.feature.favorites.api.CustomRecipeNavKey
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favoritesModule = module {
    viewModelOf(::FavoritesViewModel)
    viewModel { params ->
        val key = params.get<CustomRecipeNavKey>()
        CustomRecipeDetailViewModel(key.recipeId, get())
    }
}
