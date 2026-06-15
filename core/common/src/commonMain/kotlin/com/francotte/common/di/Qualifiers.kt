package com.francotte.common.di

import org.koin.core.qualifier.named

/** Koin qualifiers replacing the former Hilt `@Dispatcher` / `@ApplicationScope` qualifiers. */
val IoDispatcherQualifier = named("IoDispatcher")
val DefaultDispatcherQualifier = named("DefaultDispatcher")
val ApplicationScopeQualifier = named("ApplicationScope")
