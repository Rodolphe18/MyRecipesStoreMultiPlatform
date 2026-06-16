package com.francotte.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

enum class DeviceMode {
    PhonePortrait,
    PhoneLandscape,
    TabletPortrait,
    TabletLandscape,
}

/**
 * Derives the [DeviceMode] from the window container size (multiplatform, via [LocalWindowInfo]).
 * Thresholds mirror Material `WindowSizeClass`: width Compact < 600dp, Expanded >= 840dp;
 * a short window (height Compact < 480dp) is treated as a phone in landscape.
 */
@Composable
fun rememberDeviceMode(): DeviceMode {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val containerSize = windowInfo.containerSize
    return remember(containerSize, density) {
        val widthDp = with(density) { containerSize.width.toDp() }
        val heightDp = with(density) { containerSize.height.toDp() }
        val compactWidth = widthDp < 600.dp
        val expandedWidth = widthDp >= 840.dp
        val compactHeight = heightDp < 480.dp
        when {
            compactHeight -> DeviceMode.PhoneLandscape
            compactWidth -> DeviceMode.PhonePortrait
            expandedWidth -> DeviceMode.TabletLandscape
            else -> DeviceMode.TabletPortrait
        }
    }
}
