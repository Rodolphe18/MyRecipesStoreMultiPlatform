package com.francotte.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.designsystem.component.DesignAsyncImage
import com.francotte.designsystem.component.TopAppBar
import com.francotte.ui.LocalShareRecipeHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRecipeDetailScreen(
    state: CustomRecipeDetailState,
    onBackClick: () -> Unit,
) {
    val recipe = state.recipe
    val shareHandler = LocalShareRecipeHandler.current
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = recipe?.title ?: "",
                scrollBehavior = topAppBarScrollBehavior,
                navigationIconEnabled = true,
                onNavigationClick = onBackClick,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = padding.calculateTopPadding() + 12.dp,
                    bottom = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                DesignAsyncImage(
                    model = recipe?.imageUrl,
                    contentDescription = recipe?.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            }
            SectionTitle("Title")
            Text(
                text = recipe?.title ?: "",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = Dp.Hairline,
                color = Color.LightGray,
            )
            SectionTitle("Ingredients")
            recipe?.ingredients?.forEach { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = ingredient.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "x ${ingredient.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = Dp.Hairline,
                color = Color.LightGray,
            )
            SectionTitle("Instructions")
            Text(
                text = recipe?.instructions ?: "",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val text = buildString {
                        appendLine("🛒 Groceries list : ${recipe?.title}")
                        appendLine()
                        recipe?.ingredients?.forEach { (ingredient, qty, _) ->
                            appendLine("- $ingredient: $qty")
                        }
                    }
                    shareHandler.share("My groceries list for ${recipe?.title}", text)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text("Share the groceries list")
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp),
        color = MaterialTheme.colorScheme.onSurface,
    )
}
