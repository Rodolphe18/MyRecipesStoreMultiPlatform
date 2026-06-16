package com.francotte.database.internal

import com.francotte.database.model.AreaEntity
import com.francotte.database.model.CategoryEntity
import com.francotte.database.model.FullRecipeEntity
import com.francotte.database.model.IngredientEntity
import com.francotte.database.model.LightCategoryEntity
import com.francotte.database.model.LightRecipeEntity
import com.francotte.database.sql.Area
import com.francotte.database.sql.Full_category_entity
import com.francotte.database.sql.Full_recipe_entity
import com.francotte.database.sql.Ingredient
import com.francotte.database.sql.Light_recipe_entity

internal fun Area.toEntity() = AreaEntity(strArea = strArea, savedTimeStamp = savedTimeStamp)

internal fun Light_recipe_entity.toEntity() =
    LightRecipeEntity(idMeal = idMeal, strMeal = strMeal, strMealThumb = strMealThumb)

internal fun Full_category_entity.toEntity() =
    CategoryEntity(
        idCategory = idCategory,
        strCategory = strCategory,
        strCategoryThumb = strCategoryThumb,
        strCategoryDescription = strCategoryDescription,
        savedTimestamp = savedTimestamp,
    )

internal fun Ingredient.toEntity() =
    IngredientEntity(name = name, description = description, imageUrl = imageUrl, savedTimeStamp = savedTimeStamp)

internal fun Full_recipe_entity.toEntity() =
    FullRecipeEntity(
        idMeal = idMeal,
        strMeal = strMeal,
        strMealAlternate = strMealAlternate,
        strCategory = strCategory,
        strArea = strArea,
        isFavorite = isFavorite != 0L,
        strInstructions = strInstructions,
        strMealThumb = strMealThumb,
        strTags = strTags,
        strYoutube = strYoutube,
        strIngredient1 = strIngredient1, strIngredient2 = strIngredient2, strIngredient3 = strIngredient3,
        strIngredient4 = strIngredient4, strIngredient5 = strIngredient5, strIngredient6 = strIngredient6,
        strIngredient7 = strIngredient7, strIngredient8 = strIngredient8, strIngredient9 = strIngredient9,
        strIngredient10 = strIngredient10, strIngredient11 = strIngredient11, strIngredient12 = strIngredient12,
        strIngredient13 = strIngredient13, strIngredient14 = strIngredient14, strIngredient15 = strIngredient15,
        strIngredient16 = strIngredient16, strIngredient17 = strIngredient17, strIngredient18 = strIngredient18,
        strIngredient19 = strIngredient19, strIngredient20 = strIngredient20,
        strMeasure1 = strMeasure1, strMeasure2 = strMeasure2, strMeasure3 = strMeasure3,
        strMeasure4 = strMeasure4, strMeasure5 = strMeasure5, strMeasure6 = strMeasure6,
        strMeasure7 = strMeasure7, strMeasure8 = strMeasure8, strMeasure9 = strMeasure9,
        strMeasure10 = strMeasure10, strMeasure11 = strMeasure11, strMeasure12 = strMeasure12,
        strMeasure13 = strMeasure13, strMeasure14 = strMeasure14, strMeasure15 = strMeasure15,
        strMeasure16 = strMeasure16, strMeasure17 = strMeasure17, strMeasure18 = strMeasure18,
        strMeasure19 = strMeasure19, strMeasure20 = strMeasure20,
        strSource = strSource,
        strImageSource = strImageSource,
        strCreativeCommonsConfirmed = strCreativeCommonsConfirmed,
        dateModified = dateModified,
        savedTimestamp = savedTimestamp,
        isLatest = isLatest != 0L,
    )

internal fun FullRecipeEntity.toRow() =
    Full_recipe_entity(
        idMeal = idMeal,
        strMeal = strMeal,
        strMealAlternate = strMealAlternate,
        strCategory = strCategory,
        strArea = strArea,
        isFavorite = if (isFavorite) 1L else 0L,
        strInstructions = strInstructions,
        strMealThumb = strMealThumb,
        strTags = strTags,
        strYoutube = strYoutube,
        strIngredient1 = strIngredient1, strIngredient2 = strIngredient2, strIngredient3 = strIngredient3,
        strIngredient4 = strIngredient4, strIngredient5 = strIngredient5, strIngredient6 = strIngredient6,
        strIngredient7 = strIngredient7, strIngredient8 = strIngredient8, strIngredient9 = strIngredient9,
        strIngredient10 = strIngredient10, strIngredient11 = strIngredient11, strIngredient12 = strIngredient12,
        strIngredient13 = strIngredient13, strIngredient14 = strIngredient14, strIngredient15 = strIngredient15,
        strIngredient16 = strIngredient16, strIngredient17 = strIngredient17, strIngredient18 = strIngredient18,
        strIngredient19 = strIngredient19, strIngredient20 = strIngredient20,
        strMeasure1 = strMeasure1, strMeasure2 = strMeasure2, strMeasure3 = strMeasure3,
        strMeasure4 = strMeasure4, strMeasure5 = strMeasure5, strMeasure6 = strMeasure6,
        strMeasure7 = strMeasure7, strMeasure8 = strMeasure8, strMeasure9 = strMeasure9,
        strMeasure10 = strMeasure10, strMeasure11 = strMeasure11, strMeasure12 = strMeasure12,
        strMeasure13 = strMeasure13, strMeasure14 = strMeasure14, strMeasure15 = strMeasure15,
        strMeasure16 = strMeasure16, strMeasure17 = strMeasure17, strMeasure18 = strMeasure18,
        strMeasure19 = strMeasure19, strMeasure20 = strMeasure20,
        strSource = strSource,
        strImageSource = strImageSource,
        strCreativeCommonsConfirmed = strCreativeCommonsConfirmed,
        dateModified = dateModified,
        savedTimestamp = savedTimestamp,
        isLatest = if (isLatest) 1L else 0L,
    )
