package com.francotte.video

import androidx.lifecycle.ViewModel
import com.francotte.domain.YouTubeUrlParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoViewModel(youtubeUrl: String) : ViewModel() {

    val state: StateFlow<VideoState> =
        MutableStateFlow(VideoState(videoId = YouTubeUrlParser.extractVideoId(youtubeUrl)))
}

data class VideoState(
    val videoId: String = "",
)
