package com.francotte.network.model

import com.francotte.model.CustomIngredient
import com.francotte.model.CustomRecipe
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCustomIngredient(
    val name: String,
    val quantity: String,
    val measureType: String,
)

@Serializable
data class NetworkCustomRecipe(
    val id: String,
    val title: String,
    val ingredients: List<NetworkCustomIngredient>,
    val instructions: String,
    val imageUrl: String?,
)

fun NetworkCustomRecipe.asExternalModel(): CustomRecipe =
    CustomRecipe(
        id = id,
        title = title,
        ingredients = ingredients.map { it.asExternalModel() },
        instructions = instructions,
        imageUrl = imageUrl,
    )

fun NetworkCustomIngredient.asExternalModel(): CustomIngredient = CustomIngredient(name, quantity, measureType)

fun CustomRecipe.asDto(): NetworkCustomRecipe =
    NetworkCustomRecipe(
        id = id,
        title = title,
        ingredients = ingredients.map { it.asDto() },
        instructions = instructions,
        imageUrl = imageUrl,
    )

fun CustomIngredient.asDto(): NetworkCustomIngredient = NetworkCustomIngredient(name, quantity, measureType)
