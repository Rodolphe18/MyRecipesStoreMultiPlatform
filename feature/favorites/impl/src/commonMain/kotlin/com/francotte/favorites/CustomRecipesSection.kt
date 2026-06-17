package com.francotte.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.francotte.favorites.resources.Res
import com.francotte.favorites.resources.my_recipes
import com.francotte.model.CustomRecipe
import com.francotte.ui.CustomRecipeItem
import com.francotte.ui.SectionTitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomRecipesSection(
    modifier: Modifier = Modifier,
    recipes: List<CustomRecipe>?,
    onOpenRecipe: (String) -> Unit,
) {
    Column(modifier = modifier) {
        SectionTitle(
            title = stringResource(Res.string.my_recipes),
            count = recipes?.size,
            showNavIcon = false,
            paddingStart = 16.dp,
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            recipes?.let { customRecipes ->
                items(items = customRecipes, key = { it.id }) { customRecipe ->
                    CustomRecipeItem(
                        customRecipe = customRecipe,
                        onOpenRecipe = { onOpenRecipe(customRecipe.id) },
                    )
                }
            }
        }
    }
}
