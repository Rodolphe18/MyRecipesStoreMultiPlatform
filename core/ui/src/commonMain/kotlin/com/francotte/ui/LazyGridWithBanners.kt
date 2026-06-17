package com.francotte.ui

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

/**
 * A vertical grid that interleaves full-width banner slots every [bannerInterval] items.
 * Pure layout: the banner content is supplied via [bannerContent] (e.g. the shared `BannerAd`),
 * so this stays free of any ads SDK.
 */
@Composable
fun LazyGridWithBanners(
    modifier: Modifier = Modifier,
    totalItemCount: Int,
    columns: Int = 2,
    bannerInterval: Int = 4,
    state: LazyGridState,
    horizontalArrangement: Arrangement.HorizontalOrVertical,
    verticalArrangement: Arrangement.HorizontalOrVertical,
    flingBehavior: FlingBehavior,
    contentPadding: PaddingValues,
    itemKey: (index: Int) -> Any,
    itemContentType: (index: Int) -> Any = { "item" },
    bannerKey: (bannerIndex: Int) -> Any = { i -> "banner-$i" },
    bannerContentType: Any = "banner",
    bannerContent: @Composable () -> Unit,
    itemContent: @Composable LazyGridItemScope.(index: Int) -> Unit,
) {
    val bannerStep = bannerInterval + 1
    val bannerCount = totalItemCount / bannerInterval
    val totalCountWithBanners = totalItemCount + bannerCount

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = contentPadding,
        flingBehavior = flingBehavior,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) {
        items(
            count = totalCountWithBanners,
            key = { index ->
                if ((index + 1) % bannerStep == 0) {
                    bannerKey(index / bannerStep)
                } else {
                    itemKey(index - (index / bannerStep))
                }
            },
            contentType = { index ->
                if ((index + 1) % bannerStep == 0) {
                    bannerContentType
                } else {
                    itemContentType(index - (index / bannerStep))
                }
            },
            span = { index ->
                if ((index + 1) % bannerStep == 0) GridItemSpan(columns) else GridItemSpan(1)
            },
        ) { index ->
            if ((index + 1) % bannerStep == 0) {
                Box(
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            constraints.copy(maxWidth = constraints.maxWidth + 32.dp.roundToPx()),
                        )
                        layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                    },
                ) {
                    bannerContent()
                }
            } else {
                val realIndex = index - (index / bannerStep)
                if (realIndex < totalItemCount) itemContent(realIndex)
            }
        }
    }
}
