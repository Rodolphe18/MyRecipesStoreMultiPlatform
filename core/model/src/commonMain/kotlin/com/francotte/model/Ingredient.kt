package com.francotte.model

import kotlinx.datetime.Instant

class Ingredient(
    val name: String,
    val description: String,
    val imageUrl: String,
    val savedTimeStamp: Instant? = null,
)
