package com.francotte.reset.di

import com.francotte.reset.RequestResetPasswordViewModel
import com.francotte.reset.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val resetModule = module {
    viewModelOf(::RequestResetPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}
