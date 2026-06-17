package com.francotte.section.di

import com.francotte.feature.section.api.SectionNavKey
import com.francotte.section.SectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sectionModule = module {
    viewModel { params ->
        val key = params.get<SectionNavKey>()
        SectionViewModel(get(), key.sectionName)
    }
}
