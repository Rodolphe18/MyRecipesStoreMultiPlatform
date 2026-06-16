package com.francotte.navigation

/**
 * Marker for type-safe navigation routes. Concrete keys are `@Serializable` objects/classes
 * declared in each feature's `api` module, e.g. `@Serializable object HomeNavKey : NavKey`.
 */
interface NavKey
