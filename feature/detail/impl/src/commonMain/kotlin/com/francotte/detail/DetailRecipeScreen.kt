package com.francotte.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.designsystem.component.DesignAsyncImage
import com.francotte.designsystem.component.RecipeVideoPlayer
import com.francotte.detail.resources.Res
import com.francotte.detail.resources.ic_share
import com.francotte.detail.resources.ingredients
import com.francotte.detail.resources.instructions
import com.francotte.domain.YouTubeUrlParser
import com.francotte.model.LikeableRecipe
import com.francotte.model.Recipe
import com.francotte.ui.BannerAd
import com.francotte.ui.BannerPlacement
import com.francotte.ui.FavButton
import com.francotte.ui.LocalShareRecipeHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DetailRecipeScreen(
    state: DetailState,
    onAction: (DetailAction) -> Unit,
) {
    val onToggleFavorite: (LikeableRecipe) -> Unit = { onAction(DetailAction.OnToggleFavorite(it)) }
    val pageCount = state.pageCount
    val pagerState = rememberPagerState(initialPage = state.initialPage, pageCount = { pageCount })
    val deepLink = state.deeplinkRecipe
    // No TopAppBar: the title is shown in the recipe content and back is handled by the host.
    Scaffold { padding ->
        if (deepLink != null) {
            RecipeContent(
                likeableRecipe = deepLink,
                onToggleFavorite = onToggleFavorite,
                topPadding = padding.calculateTopPadding() + 12.dp,
                modifier = Modifier
                    .testTag("full_detail_screen")
                    .semantics { contentDescription = "full_detail_screen" },
            )
        } else {
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.settledPage }
                    .distinctUntilChanged()
                    .collectLatest { newPage -> onAction(DetailAction.OnPageChanged(newPage)) }
            }
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxSize(),
            ) { index ->
                state.recipes[index]?.let { likeableRecipe ->
                    RecipeContent(
                        likeableRecipe = likeableRecipe,
                        onToggleFavorite = onToggleFavorite,
                        topPadding = padding.calculateTopPadding() + 12.dp,
                    )
                }
            }
        }
    }
}

@Composable
internal fun RecipeContent(
    likeableRecipe: LikeableRecipe,
    onToggleFavorite: (LikeableRecipe) -> Unit,
    topPadding: Dp,
    modifier: Modifier = Modifier,
) {
    val shareHandler = LocalShareRecipeHandler.current
    val recipe = likeableRecipe.recipe as Recipe
    val ingredients = remember(recipe) { recipe.ingredientPairs() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = topPadding, bottom = 12.dp),
    ) {
        DetailScreenMainSectionTitle(likeableRecipe)
        Spacer(modifier = Modifier.height(8.dp))
        DetailVideoScreen(likeableRecipe)
        Spacer(modifier = Modifier.height(8.dp))
        BannerAd(placement = BannerPlacement.RECIPE_POS_1, horizontalPadding = 12.dp)
        Spacer(modifier = Modifier.height(8.dp))
        DetailScreenIngredientTitle(likeableRecipe, Res.string.ingredients, onToggleFavorite)
        IngredientRow(ingredients)
        DetailRecipeShareRecipeButton(likeableRecipe, ingredients) { subject, text ->
            shareHandler.share(subject, text)
        }
        BannerAd(placement = BannerPlacement.RECIPE_POS_2, horizontalPadding = 12.dp)
        Spacer(modifier = Modifier.height(8.dp))
        DetailScreenSectionTitle(Res.string.instructions)
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = recipe.strInstructions.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun DetailVideoScreen(likeableRecipe: LikeableRecipe) {
    val youtubeUrl = (likeableRecipe.recipe as Recipe).strYoutube
    val videoId = remember(youtubeUrl) { YouTubeUrlParser.extractVideoId(youtubeUrl) }
    if (videoId.isNotBlank()) {
        RecipeVideoPlayer(
            videoId = videoId,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        )
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            DesignAsyncImage(
                model = likeableRecipe.recipe.strMealThumb,
                width = maxWidth,
                height = 200.dp,
                contentDescription = "Image de ${likeableRecipe.recipe.strMeal}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun DetailRecipeShareRecipeButton(
    likeableRecipe: LikeableRecipe,
    ingredients: List<Pair<String, String>>,
    onShare: (subject: String, text: String) -> Unit,
) {
    Button(
        onClick = {
            val shoppingListText = buildString {
                appendLine("🛒 Groceries list : ${likeableRecipe.recipe.strMeal}")
                appendLine()
                ingredients.forEach { (ingredient, measure) ->
                    appendLine("- $ingredient: $measure")
                }
            }
            onShare("My groceries list for ${likeableRecipe.recipe.strMeal}", shoppingListText)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_share),
            contentDescription = "Share",
            modifier = Modifier.padding(end = 8.dp),
        )
        Text("Share the groceries list")
    }
}

@Composable
private fun DetailScreenMainSectionTitle(likeableRecipe: LikeableRecipe) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = likeableRecipe.recipe.strMeal,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
}

@Composable
private fun DetailScreenIngredientTitle(
    likeableRecipe: LikeableRecipe,
    stringRes: StringResource,
    onToggleFavorite: (LikeableRecipe) -> Unit,
) {
    Row(Modifier.padding(horizontal = 12.dp)) {
        Text(
            text = stringResource(stringRes),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .weight(1f),
        )
        FavButton(
            modifier = Modifier.padding(8.dp),
            onToggleFavorite = { onToggleFavorite(likeableRecipe) },
            syncState = likeableRecipe.favoriteState,
        )
    }
}

@Composable
private fun DetailScreenSectionTitle(stringRes: StringResource) {
    Text(
        text = stringResource(stringRes),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 12.dp),
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
private fun IngredientRow(ingredients: List<Pair<String, String>>) {
    ingredients.forEach { (ingredient, measure) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = ingredient,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = measure,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
