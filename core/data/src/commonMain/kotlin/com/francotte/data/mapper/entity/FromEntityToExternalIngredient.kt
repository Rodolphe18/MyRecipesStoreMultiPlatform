package com.francotte.data.mapper.entity

import com.francotte.database.model.IngredientEntity
import com.francotte.model.Ingredient
import kotlinx.datetime.Instant

fun IngredientEntity.asExternalModel() =
    Ingredient(
        name = name,
        description = description,
        imageUrl = imageUrl,
        savedTimeStamp = savedTimeStamp?.let { Instant.fromEpochMilliseconds(it) },
    )
