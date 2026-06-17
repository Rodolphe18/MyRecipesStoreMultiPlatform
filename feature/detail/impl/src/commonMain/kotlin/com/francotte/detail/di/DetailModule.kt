package com.francotte.detail.di

import com.francotte.detail.DetailRecipeViewModel
import com.francotte.feature.detail.api.DetailRecipeNavKey
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Detail DI. The nav args are supplied at resolution time via `parametersOf(navKey)`
 * (replacing Hilt's `@AssistedInject`).
 */
val detailModule = module {
    viewModel { params ->
        val key = params.get<DetailRecipeNavKey>()
        DetailRecipeViewModel(get(), key.ids, key.index, key.title)
    }
}
