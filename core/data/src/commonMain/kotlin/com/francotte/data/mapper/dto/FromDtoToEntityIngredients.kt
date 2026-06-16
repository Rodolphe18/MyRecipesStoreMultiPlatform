package com.francotte.data.mapper.dto

import com.francotte.database.model.IngredientEntity
import com.francotte.network.model.NetworkIngredient
import kotlinx.datetime.Clock

fun NetworkIngredient.asEntity(): IngredientEntity =
    IngredientEntity(
        name = strIngredient.orEmpty().trim(),
        description = strDescription ?: "",
        imageUrl = strThumb ?: "",
        savedTimeStamp = Clock.System.now(),
    )
