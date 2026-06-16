package com.francotte.database.crossrefs

import com.francotte.database.model.AreaEntity
import com.francotte.database.model.IngredientEntity
import com.francotte.database.model.LightCategoryEntity
import com.francotte.database.model.LightRecipeEntity

data class AreaWithRecipes(
    val area: AreaEntity,
    val recipes: List<LightRecipeEntity>,
)

data class CategoryWithRecipes(
    val category: LightCategoryEntity,
    val recipes: List<LightRecipeEntity>,
)

data class IngredientWithRecipes(
    val ingredient: IngredientEntity,
    val recipes: List<LightRecipeEntity>,
)
