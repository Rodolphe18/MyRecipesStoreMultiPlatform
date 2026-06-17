package com.francotte.detail

import com.francotte.model.Recipe

/**
 * The native screen read `strIngredientN`/`strMeasureN` via Java reflection, which doesn't exist
 * on Kotlin/Native. This maps the 20 fixed fields explicitly, keeping only non-blank ingredients.
 */
internal fun Recipe.ingredientPairs(): List<Pair<String, String>> {
    val ingredients = listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20,
    )
    val measures = listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20,
    )
    return ingredients.mapIndexedNotNull { i, ingredient ->
        if (!ingredient.isNullOrBlank()) ingredient to (measures[i] ?: "") else null
    }
}
