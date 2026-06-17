package com.francotte.register.di

import com.francotte.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val registerModule = module {
    viewModelOf(::RegisterViewModel)
}
