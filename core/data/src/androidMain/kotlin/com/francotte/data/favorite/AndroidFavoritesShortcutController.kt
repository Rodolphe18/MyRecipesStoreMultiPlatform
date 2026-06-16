package com.francotte.data.favorite

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.francotte.data.R

const val SHORTCUT_ID_FAVORITES = "shortcut_favorites"

/** Android implementation of [FavoritesShortcutController] using dynamic app shortcuts. */
class AndroidFavoritesShortcutController(private val context: Context) : FavoritesShortcutController {

    override fun setEnabled(enabled: Boolean) {
        if (enabled) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "myapp://favorites".toUri()
                putExtra("is_shortcut", true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_ID_FAVORITES)
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_favorite))
                .setShortLabel("favorites")
                .setLongLabel("favorites")
                .setIntent(intent)
                .build()
            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
        } else {
            ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(SHORTCUT_ID_FAVORITES))
        }
    }
}
