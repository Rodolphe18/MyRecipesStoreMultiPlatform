package com.francotte.myrecipesstorekmp.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.francotte.auth.SessionRepository
import com.francotte.categories.categoriesScreen
import com.francotte.categories.categoryScreen
import com.francotte.designsystem.theme.FoodTheme
import com.francotte.detail.detailScreen
import com.francotte.favorites.customRecipeScreen
import com.francotte.favorites.favoritesScreen
import com.francotte.feature.categories.api.CategoriesNavKey
import com.francotte.feature.categories.api.navigateToCategory
import com.francotte.feature.detail.api.navigateToDetail
import com.francotte.feature.favorites.api.FavoritesNavKey
import com.francotte.feature.favorites.api.navigateToCustomRecipe
import com.francotte.feature.home.api.HomeNavKey
import com.francotte.feature.login.api.LoginNavKey
import com.francotte.feature.search.api.SearchNavKey
import com.francotte.feature.search.api.navigateToSearchMode
import com.francotte.feature.search.api.navigateToSearchRecipes
import com.francotte.feature.register.api.navigateToRegister
import com.francotte.feature.reset.api.navigateToRequestReset
import com.francotte.feature.section.api.navigateToSection
import com.francotte.feature.video.api.navigateToVideo
import com.francotte.home.homeScreen
import com.francotte.profile.profileScreen
import com.francotte.register.registerScreen
import com.francotte.reset.requestResetScreen
import com.francotte.reset.resetPasswordScreen
import com.francotte.section.sectionScreen
import com.francotte.video.videoScreen
import com.francotte.login.loginScreen
import com.francotte.search.result_mode.searchModeScreen
import com.francotte.search.result_recipe.searchRecipesScreen
import com.francotte.search.searchScreen
import com.francotte.navigation.rememberNavigator
import com.francotte.ui.LocalShareRecipeHandler
import com.francotte.ui.LocalSnackbarHostState
import com.francotte.ui.ShareRecipeHandler
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    val navigator = rememberNavigator()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessionRepository = koinInject<SessionRepository>()
    val isAuthenticated by sessionRepository.isAuthenticated.collectAsStateWithLifecycle()
    val shareHandler = remember(context) {
        ShareRecipeHandler { subject, text ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Share the groceries list with"))
        }
    }

    val backStackEntry by navigator.navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val onHome = destination?.hierarchy?.any { it.hasRoute(HomeNavKey::class) } == true
    val onCategories = destination?.hierarchy?.any { it.hasRoute(CategoriesNavKey::class) } == true
    val onSearch = destination?.hierarchy?.any { it.hasRoute(SearchNavKey::class) } == true
    val onFavorites = destination?.hierarchy?.any { it.hasRoute(FavoritesNavKey::class) } == true
    val onLogin = destination?.hierarchy?.any { it.hasRoute(LoginNavKey::class) } == true
    val showBottomBar = onHome || onCategories || onSearch || onFavorites || onLogin

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
        LocalShareRecipeHandler provides shareHandler,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = onHome,
                            onClick = { navigator.navigateTopLevel(HomeNavKey) },
                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                        )
                        NavigationBarItem(
                            selected = onCategories,
                            onClick = { navigator.navigateTopLevel(CategoriesNavKey) },
                            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Categories") },
                            label = { Text("Categories") },
                        )
                        NavigationBarItem(
                            selected = onSearch,
                            onClick = { navigator.navigateTopLevel(SearchNavKey) },
                            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                            label = { Text("Search") },
                        )
                        // Auth-gated last tab: Favorites when logged in, Login otherwise.
                        if (isAuthenticated) {
                            NavigationBarItem(
                                selected = onFavorites,
                                onClick = { navigator.navigateTopLevel(FavoritesNavKey) },
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
                                label = { Text("Favorites") },
                            )
                        } else {
                            NavigationBarItem(
                                selected = onLogin,
                                onClick = { navigator.navigateTopLevel(LoginNavKey) },
                                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Login") },
                                label = { Text("Login") },
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navigator.navController,
                startDestination = HomeNavKey,
                modifier = Modifier.padding(innerPadding),
            ) {
                homeScreen(
                    onRecipeClick = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onToggleFavorite = { },
                    onOpenSection = { sectionName -> navigator.navigateToSection(sectionName) },
                    onVideoButtonClick = { youtubeUrl -> navigator.navigateToVideo(youtubeUrl) },
                )
                categoriesScreen(
                    onOpenCategory = { category -> navigator.navigateToCategory(category.strCategory) },
                )
                categoryScreen(
                    onOpenRecipe = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onToggleFavorite = { },
                    onBack = navigator::goBack,
                )
                searchScreen(
                    onSearchModeSelected = { mode -> navigator.navigateToSearchMode(mode) },
                    onSearchTypeClick = { item, mode -> navigator.navigateToSearchRecipes(item, mode) },
                    onOpenRecipe = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onToggleFavorite = { },
                )
                searchModeScreen(
                    onItemSelected = { item, mode -> navigator.navigateToSearchRecipes(item, mode) },
                    onBack = navigator::goBack,
                )
                searchRecipesScreen(
                    onOpenRecipe = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onToggleFavorite = { },
                    onBack = navigator::goBack,
                )
                favoritesScreen(
                    onRecipeClick = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onCustomRecipeClick = { id -> navigator.navigateToCustomRecipe(id) },
                    onToggleFavorite = { },
                )
                customRecipeScreen(onBack = navigator::goBack)
                sectionScreen(
                    onOpenRecipe = { ids, index, title -> navigator.navigateToDetail(ids, index, title) },
                    onToggleFavorite = { },
                    onBack = navigator::goBack,
                )
                videoScreen()
                loginScreen(
                    onRegister = { navigator.navigateToRegister() },
                    onResetPassword = { navigator.navigateToRequestReset() },
                    onLoginSuccess = { navigator.navigateTopLevel(FavoritesNavKey) },
                    // Google CredentialManager integration is deferred; surface a notice for now.
                    onGoogleSignIn = {
                        scope.launch { snackbarHostState.showSnackbar("Google sign-in not available yet") }
                    },
                )
                registerScreen(
                    onBack = navigator::goBack,
                    onRegistered = { navigator.navigateTopLevel(FavoritesNavKey) },
                )
                requestResetScreen(onBack = navigator::goBack)
                resetPasswordScreen()
                profileScreen(onBack = navigator::goBack)
                detailScreen(
                    onBack = navigator::goBack,
                    onToggleFavorite = { },
                )
            }
        }
    }
}
