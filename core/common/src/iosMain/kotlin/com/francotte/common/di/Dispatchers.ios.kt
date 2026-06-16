package com.francotte.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Dispatchers.IO does not exist on Kotlin/Native; Default is the closest standard choice.
actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
