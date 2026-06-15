package com.francotte.common.di

import kotlinx.coroutines.CoroutineDispatcher

/**
 * The IO dispatcher is platform-specific (`Dispatchers.IO` only exists on JVM/Native),
 * so it is provided through expect/actual.
 */
expect val ioDispatcher: CoroutineDispatcher
