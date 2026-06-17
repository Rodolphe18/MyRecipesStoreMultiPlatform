package com.francotte.categories.di

import com.francotte.categories.CategoriesViewModel
import com.francotte.categories.CategoryViewModel
import com.francotte.feature.categories.api.CategoryNavKey
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val categoriesModule = module {
    viewModelOf(::CategoriesViewModel)
    viewModel { params ->
        val key = params.get<CategoryNavKey>()
        CategoryViewModel(key.category, get(), get())
    }
}
