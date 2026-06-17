package com.francotte.designsystem.component

import androidx.compose.runtime.Composable

/**
 * Hides the system bars for an immersive (fullscreen video) experience.
 * Android drives the WindowInsetsController; other platforms are a no-op.
 */
@Composable
expect fun HideNavigationBar()
