package com.francotte.domain.di

import com.francotte.domain.GetSearchContentsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetSearchContentsUseCase(get(), get()) }
}
