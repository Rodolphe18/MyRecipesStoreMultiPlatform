package com.francotte.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shared [SnackbarHostState] so any feature screen can show a snackbar without owning the host.
 * Provided once at the app scaffold level.
 */
val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided. Wrap the content in a CompositionLocalProvider.")
}

/** Ad banner slots used across feature screens. */
enum class BannerPlacement {
    HOME_POS_1,
    HOME_POS_2,
    RECIPE_POS_1,
    RECIPE_POS_2,
    FOOD_LIST,
    SEARCH,
}

/**
 * Shares plain text (e.g. a recipe's groceries list). Android provides an `Intent`-backed handler
 * via [LocalShareRecipeHandler]; other platforms (and previews) fall back to a no-op. Feature code
 * in `commonMain` only builds the text and calls [LocalShareRecipeHandler].
 */
fun interface ShareRecipeHandler {
    fun share(subject: String, text: String)
}

val LocalShareRecipeHandler = staticCompositionLocalOf<ShareRecipeHandler> {
    ShareRecipeHandler { _, _ -> /* no-op */ }
}

/**
 * Renders a banner ad for a given [BannerPlacement].
 *
 * Ads stay Android-only for now: the Android app provides an AdMob-backed renderer through
 * [LocalBannerRenderer], while other platforms (and Compose previews) fall back to a no-op.
 * Feature code in `commonMain` only ever calls [BannerAd] and never sees the ads SDK.
 */
interface BannerRenderer {
    @Composable
    fun Render(placement: BannerPlacement, modifier: Modifier, horizontalPadding: Dp)
}

private object NoOpBannerRenderer : BannerRenderer {
    @Composable
    override fun Render(placement: BannerPlacement, modifier: Modifier, horizontalPadding: Dp) {
        // No banner on platforms without an ads implementation.
    }
}

val LocalBannerRenderer = staticCompositionLocalOf<BannerRenderer> { NoOpBannerRenderer }

/** Common entry point used by feature screens; delegates to the platform [BannerRenderer]. */
@Composable
fun BannerAd(
    placement: BannerPlacement,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
) {
    LocalBannerRenderer.current.Render(placement, modifier, horizontalPadding)
}
