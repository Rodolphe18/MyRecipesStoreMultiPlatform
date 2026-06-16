package com.francotte.data.di

import com.francotte.common.di.ApplicationScopeQualifier
import com.francotte.data.favorite.FavoriteManager
import com.francotte.data.interfaces.CategoriesRepository
import com.francotte.data.interfaces.FavoriteHelper
import com.francotte.data.interfaces.FavoritesRepository
import com.francotte.data.interfaces.HomeRepository
import com.francotte.data.interfaces.IngredientsAndAreasRepository
import com.francotte.data.interfaces.OfflineFirstFavoritesRepository
import com.francotte.data.interfaces.OfflineFirstFullRecipeRepository
import com.francotte.data.interfaces.SearchContentsRepository
import com.francotte.data.interfaces.UserDataRepository
import com.francotte.data.interfaces.UserFullRecipeRepository
import com.francotte.data.interfaces.UserHomeRepository
import com.francotte.data.repository.CompositeUserFullRecipeRepository
import com.francotte.data.repository.CompositeUserHomeRepository
import com.francotte.data.repository.DefaultSearchContentsRepository
import com.francotte.data.repository.FavoritesRepositoryImpl
import com.francotte.data.repository.LocalUserDataRepository
import com.francotte.data.repository.OfflineFirstCategoriesRepositoryImpl
import com.francotte.data.repository.OfflineFirstFavoritesRepositoryImpl
import com.francotte.data.repository.OfflineFirstFullRecipeRepositoryImpl
import com.francotte.data.repository.OfflineFirstHomeRepository
import com.francotte.data.repository.OfflineFirstIngredientsAndAreasRepositoryImpl
import org.koin.dsl.module

/**
 * Common data DI (repositories). The platform-specific bindings — [com.francotte.data.util.NetworkMonitor],
 * [com.francotte.data.favorite.FavoritesShortcutController] and [com.francotte.data.sync.SyncScheduler] —
 * are provided by `androidDataModule`.
 */
val dataModule = module {
    single<UserDataRepository> { LocalUserDataRepository(get()) }

    single { OfflineFirstHomeRepository(get(), get(), get()) }
    single<HomeRepository> { get<OfflineFirstHomeRepository>() }

    single<CategoriesRepository> { OfflineFirstCategoriesRepositoryImpl(get(), get()) }
    single<OfflineFirstFullRecipeRepository> { OfflineFirstFullRecipeRepositoryImpl(get(), get()) }
    single<UserFullRecipeRepository> { CompositeUserFullRecipeRepository(get(), get()) }
    single<OfflineFirstFavoritesRepository> { OfflineFirstFavoritesRepositoryImpl(get(), get(), get(), get()) }
    single<UserHomeRepository> { CompositeUserHomeRepository(get(), get()) }
    single<IngredientsAndAreasRepository> {
        OfflineFirstIngredientsAndAreasRepositoryImpl(get(), get(), get(), get(), get())
    }
    single<SearchContentsRepository> {
        DefaultSearchContentsRepository(get(), get(), get(), get())
    }

    single {
        FavoriteManager(
            coroutineScope = get(ApplicationScopeQualifier),
            api = get(),
            networkMonitor = get(),
            foodPreferencesDataSource = get(),
            syncScheduler = get(),
            favoritesShortcutController = get(),
        )
    }
    single<FavoriteHelper> { get<FavoriteManager>() }

    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }
}
