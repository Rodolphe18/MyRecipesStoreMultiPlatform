package com.francotte.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage

/**
 * Multiplatform async image (Coil 3). The placeholder defaults to a solid color; callers can
 * pass a [Painter] (e.g. a `compose.resources` drawable) once resources are migrated.
 */
@Composable
fun DesignAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    height: Dp = Dp.Unspecified,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    placeholder: Painter = ColorPainter(Color.LightGray),
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.background(Color.LightGray.copy(alpha = 0.7f)),
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = contentScale,
        alignment = alignment,
    )
}
