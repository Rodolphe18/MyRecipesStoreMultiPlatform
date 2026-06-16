package com.francotte.auth.di

import com.francotte.auth.AndroidCredentialStateClearer
import com.francotte.auth.CredentialStateClearer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android-specific auth DI: Credential Manager state clearer. */
val androidAuthModule = module {
    single<CredentialStateClearer> { AndroidCredentialStateClearer(androidContext()) }
}
