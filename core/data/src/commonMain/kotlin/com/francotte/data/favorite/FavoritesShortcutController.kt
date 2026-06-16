package com.francotte.data.favorite

/**
 * Controls the platform "favorites" app shortcut. Implemented per platform
 * (Android uses `ShortcutManagerCompat`); a no-op elsewhere.
 */
interface FavoritesShortcutController {
    fun setEnabled(enabled: Boolean)
}
