package com.francotte.database.crossrefs

data class RecipeAreaCrossRef(
    val strArea: String,
    val idMeal: String,
    val savedTimestamp: Long,
)

data class RecipeCategoryCrossRef(
    val strCategory: String,
    val idMeal: String,
    val savedTimestamp: Long,
)

data class RecipeIngredientCrossRef(
    val ingredientName: String,
    val idMeal: String,
    val savedTimestamp: Long,
)
