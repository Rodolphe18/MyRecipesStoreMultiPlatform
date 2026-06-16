package com.francotte.database.model

data class LightCategoryEntity(val strCategory: String)

data class CategoryEntity(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String,
    val savedTimestamp: Long? = null,
)
