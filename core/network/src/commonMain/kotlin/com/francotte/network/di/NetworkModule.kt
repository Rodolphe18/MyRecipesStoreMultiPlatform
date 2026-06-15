package com.francotte.network.di

import com.francotte.network.api.AuthApi
import com.francotte.network.api.FavoriteApi
import com.francotte.network.api.RecipeApi
import com.francotte.network.utils.FOOD_BASE_URL
import com.francotte.network.utils.USER_BASE_URL
import org.koin.dsl.module

/**
 * Common network DI. The [io.ktor.client.HttpClient] itself is provided by the
 * platform-specific module (see `androidNetworkModule`) because its engine and
 * configuration (DNS, cache, timeouts) are platform dependent.
 */
val networkModule = module {
    single { provideJson() }
    single { RecipeApi(get(), FOOD_BASE_URL) }
    single { AuthApi(get(), USER_BASE_URL) }
    single { FavoriteApi(get(), USER_BASE_URL) }
}
