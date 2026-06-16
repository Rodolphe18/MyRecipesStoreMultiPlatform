package com.francotte.auth.di

import com.francotte.auth.CredentialStateClearer
import org.koin.dsl.module

/**
 * iOS auth DI. There is no Android-style Credential Manager on iOS, so clearing credential
 * state is a no-op for now (Google sign-in on iOS would use its own SDK).
 */
val iosAuthModule = module {
    single<CredentialStateClearer> { NoOpCredentialStateClearer() }
}

private class NoOpCredentialStateClearer : CredentialStateClearer {
    override suspend fun clear() = Unit
}
