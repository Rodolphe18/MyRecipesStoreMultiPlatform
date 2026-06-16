package com.francotte.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Thin wrapper around [NavHostController] that centralises the app's navigation behaviours.
 *
 * Replaces the former Navigation3 multi-back-stack `Navigator`: with Navigation Compose the
 * controller owns the back stack, and bottom-bar tabs use the standard "multiple back stacks"
 * pattern (saveState/restoreState) via [navigateTopLevel].
 */
class Navigator(val navController: NavHostController) {

    /** Navigate to a nested destination (pushes onto the current stack). */
    fun navigate(route: NavKey) {
        navController.navigate(route)
    }

    /**
     * Switch to a top-level destination (bottom-bar tab), preserving each tab's own back stack.
     */
    fun navigateTopLevel(route: NavKey) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun goBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNavigator(
    navController: NavHostController = rememberNavController(),
): Navigator = remember(navController) { Navigator(navController) }
