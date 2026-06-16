package com.francotte.database.model

data class IngredientEntity(
    val name: String,
    val description: String,
    val imageUrl: String,
    val savedTimeStamp: Long? = null,
)
