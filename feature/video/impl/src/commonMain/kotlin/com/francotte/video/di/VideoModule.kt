package com.francotte.video.di

import com.francotte.feature.video.api.VideoNavKey
import com.francotte.video.VideoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val videoModule = module {
    viewModel { params ->
        val key = params.get<VideoNavKey>()
        VideoViewModel(key.youTubeUrl)
    }
}
