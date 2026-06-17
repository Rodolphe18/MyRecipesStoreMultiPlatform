package com.francotte.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Plays a YouTube video by its [videoId].
 *
 * Android renders it in a WebView/iframe ([YouTubeWebViewPlayer]); iOS will use a WKWebView.
 * The player is platform-specific because it relies on a native web view, so it lives behind
 * an expect/actual rather than in shared UI code.
 */
@Composable
expect fun RecipeVideoPlayer(videoId: String, modifier: Modifier = Modifier)
