package com.francotte.myrecipesstorekmp.android.di

import com.francotte.auth.di.androidAuthModule
import com.francotte.auth.di.authModule
import com.francotte.common.di.androidCommonModule
import com.francotte.common.di.coroutinesModule
import com.francotte.data.di.androidDataModule
import com.francotte.data.di.dataModule
import com.francotte.database.di.androidDatabaseModule
import com.francotte.database.di.daoModule
import com.francotte.datastore.di.androidDatastoreModule
import com.francotte.datastore.di.datastoreModule
import com.francotte.domain.di.domainModule
import com.francotte.network.di.androidNetworkModule
import com.francotte.network.di.networkModule
import com.francotte.ui.di.syncModule
import org.koin.core.module.Module

/** All Koin modules composing the foundation graph (common + per-platform Android). */
val appModules: List<Module> = listOf(
    coroutinesModule,
    androidCommonModule,
    networkModule,
    androidNetworkModule,
    daoModule,
    androidDatabaseModule,
    datastoreModule,
    androidDatastoreModule,
    dataModule,
    androidDataModule,
    domainModule,
    authModule,
    androidAuthModule,
    syncModule,
)
