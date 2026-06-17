package com.francotte.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun RecipeVideoPlayer(videoId: String, modifier: Modifier) {
    YouTubeWebViewPlayer(videoId = videoId, modifier = modifier)
}
