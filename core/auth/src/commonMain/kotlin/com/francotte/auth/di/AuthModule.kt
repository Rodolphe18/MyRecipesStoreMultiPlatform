package com.francotte.auth.di

import com.francotte.auth.AuthEventBus
import com.francotte.auth.AuthSynchronizer
import com.francotte.auth.PasswordResetManager
import com.francotte.auth.PasswordResetRepository
import com.francotte.auth.RegistrationManager
import com.francotte.auth.RegistrationRepository
import com.francotte.auth.SessionManager
import com.francotte.auth.SessionRepository
import com.francotte.auth.strategy.EmailPasswordLoginStrategy
import com.francotte.auth.strategy.GoogleLoginStrategy
import com.francotte.auth.strategy.LoginAuthStrategy
import com.francotte.common.di.ApplicationScopeQualifier
import org.koin.dsl.module

/**
 * Common auth DI. The platform-specific [com.francotte.auth.CredentialStateClearer]
 * is provided by `androidAuthModule`.
 */
val authModule = module {
    single { AuthEventBus() }
    single { AuthSynchronizer(get(), get(), get()) }

    single { EmailPasswordLoginStrategy(get(), get()) }
    single { GoogleLoginStrategy(get(), get()) }
    single<Set<LoginAuthStrategy>> {
        setOf(get<EmailPasswordLoginStrategy>(), get<GoogleLoginStrategy>())
    }

    single<RegistrationRepository> { RegistrationManager(get(), get()) }
    single<PasswordResetRepository> { PasswordResetManager(get()) }

    single<SessionRepository> {
        SessionManager(
            loginStrategies = get(),
            authSynchronizer = get(),
            preferences = get(),
            dao = get(),
            eventBus = get(),
            api = get(),
            credentialStateClearer = get(),
            coroutineScope = get(ApplicationScopeQualifier),
        )
    }
}
