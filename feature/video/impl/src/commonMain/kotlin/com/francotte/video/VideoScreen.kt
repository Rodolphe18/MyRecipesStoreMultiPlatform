package com.francotte.video

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.francotte.designsystem.component.HideNavigationBar
import com.francotte.designsystem.component.RecipeVideoPlayer

@Composable
fun VideoFullScreen(videoId: String) {
    HideNavigationBar()
    if (videoId.isNotBlank()) {
        RecipeVideoPlayer(
            videoId = videoId,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
