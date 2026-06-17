package com.francotte.ui

import com.francotte.data.interfaces.HomeRepository
import com.francotte.data.interfaces.UserHomeRepository

/**
 * Platform-agnostic home synchronisation logic: the "what" to refresh on startup.
 *
 * The "when/how" of triggering is platform-specific: Android schedules it through a WorkManager
 * [HomeSyncWorker] (network-constrained, retried); iOS launches [sync] in an app-scoped coroutine
 * at launch. Both delegate here so the refresh policy lives in one place.
 */
class HomeSyncer(
    private val homeRepository: HomeRepository,
    private val userHomeRepository: UserHomeRepository,
) {
    suspend fun sync() {
        homeRepository.refreshLatestRecipes(force = false)
        userHomeRepository.apply {
            refreshMultipleFoodAreaSection(force = false)
            refreshSpecificFoodAreaSection("Japanese", false)
            refreshSpecificFoodAreaSection("British", false)
        }
    }
}
