package com.francotte.home.di

import com.francotte.home.HomeViewModel
import com.francotte.home.delegate.AreasRecipesDelegate
import com.francotte.home.delegate.AreasRecipesDelegateImpl
import com.francotte.home.delegate.EnglishRecipesDelegate
import com.francotte.home.delegate.EnglishRecipesDelegateImpl
import com.francotte.home.delegate.JapaneseRecipesDelegate
import com.francotte.home.delegate.JapaneseRecipesDelegateImpl
import com.francotte.home.delegate.LatestRecipesDelegate
import com.francotte.home.delegate.LatestRecipesDelegateImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Home feature DI. Replaces the Hilt `HomeDelegateModule` (ViewModelComponent scoped).
 * Delegates are plain factories; the ViewModel is registered via Koin's multiplatform
 * `viewModelOf` so `koinViewModel()` can resolve it on Android and iOS.
 */
val homeModule = module {
    factoryOf(::LatestRecipesDelegateImpl) bind LatestRecipesDelegate::class
    factoryOf(::JapaneseRecipesDelegateImpl) bind JapaneseRecipesDelegate::class
    factoryOf(::AreasRecipesDelegateImpl) bind AreasRecipesDelegate::class
    factoryOf(::EnglishRecipesDelegateImpl) bind EnglishRecipesDelegate::class

    viewModelOf(::HomeViewModel)
}
