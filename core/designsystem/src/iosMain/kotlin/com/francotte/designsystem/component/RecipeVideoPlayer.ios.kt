package com.francotte.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Placeholder until a WKWebView-backed player is wired on iOS. Renders an empty surface so the
 * detail layout stays intact; the screen already falls back to the recipe thumbnail when there is
 * no video id.
 */
@Composable
actual fun RecipeVideoPlayer(videoId: String, modifier: Modifier) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant))
}
