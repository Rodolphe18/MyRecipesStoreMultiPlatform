package com.francotte.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.francotte.common.counters.ScreenCounter
import com.francotte.feature.video.api.VideoNavKey
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.videoScreen() {
    composable<VideoNavKey> { entry ->
        val key = entry.toRoute<VideoNavKey>()
        VideoRoute(navKey = key)
    }
}

@Composable
internal fun VideoRoute(navKey: VideoNavKey) {
    val viewModel: VideoViewModel = koinViewModel { parametersOf(navKey) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    VideoFullScreen(videoId = state.videoId)
    LaunchedEffect(Unit) {
        ScreenCounter.increment()
    }
}
