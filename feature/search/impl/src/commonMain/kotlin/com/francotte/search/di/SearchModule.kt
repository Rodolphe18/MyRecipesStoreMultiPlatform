package com.francotte.search.di

import com.francotte.feature.search.api.SearchModeNavKey
import com.francotte.feature.search.api.SearchRecipesNavKey
import com.francotte.search.SearchViewModel
import com.francotte.search.result_mode.SearchModeViewModel
import com.francotte.search.result_recipe.SearchRecipesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { params ->
        val key = params.get<SearchModeNavKey>()
        SearchModeViewModel(get(), key.searchMode)
    }
    viewModel { params ->
        val key = params.get<SearchRecipesNavKey>()
        SearchRecipesViewModel(get(), get(), get(), key.mode, key.item)
    }
}
