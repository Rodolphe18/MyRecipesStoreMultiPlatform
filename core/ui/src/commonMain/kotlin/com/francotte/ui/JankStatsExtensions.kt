package com.francotte.ui

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable

/**
 * No-op on common/iOS. JankStats frame instrumentation is Android-only and non-critical;
 * an Android `actual` (androidx.metrics) can be reintroduced later via expect/actual if needed.
 */
@Composable
fun TrackScrollJank(scrollableState: ScrollableState, stateName: String) {
    // no-op
}
