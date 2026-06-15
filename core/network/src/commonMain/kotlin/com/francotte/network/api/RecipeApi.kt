package com.francotte.network.api

import com.francotte.network.model.NetworkAreas
import com.francotte.network.model.NetworkCategories
import com.francotte.network.model.NetworkIngredients
import com.francotte.network.model.NetworkRecipeResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipeApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    // Latest Meals
    suspend fun getLatestMeals(): NetworkRecipeResult =
        client.get("${baseUrl}latest.php").body()

    // Lookup a selection of 10 random meals
    suspend fun getRandomMealsSelection(): NetworkRecipeResult =
        client.get("${baseUrl}randomselection.php").body()

    // Search meal by name
    suspend fun getMealByName(name: String): NetworkRecipeResult =
        client.get("${baseUrl}search.php") { parameter("s", name) }.body()

    // Lookup full meal details by id
    suspend fun getMealDetail(id: Long): NetworkRecipeResult =
        client.get("${baseUrl}lookup.php") { parameter("i", id) }.body()

    // Lookup a single random meal
    suspend fun getRandomMeal(): NetworkRecipeResult =
        client.get("${baseUrl}random.php").body()

    // List all meal categories
    suspend fun getAllMealCategories(): NetworkCategories =
        client.get("${baseUrl}categories.php").body()

    // List all Areas
    suspend fun getAllAreas(list: String = "list"): NetworkAreas =
        client.get("${baseUrl}list.php") { parameter("a", list) }.body()

    // List all Ingredients
    suspend fun getAllIngredients(list: String = "list"): NetworkIngredients =
        client.get("${baseUrl}list.php") { parameter("i", list) }.body()

    // List all Categories
    suspend fun getAllCategories(list: String = "list"): NetworkCategories =
        client.get("${baseUrl}list.php") { parameter("c", list) }.body()

    // Filter by multi-ingredient
    suspend fun getRecipesListByMultiIngredients(ingredient: String): NetworkRecipeResult =
        client.get("${baseUrl}filter.php") { parameter("i", ingredient) }.body()

    // Filter by Category
    suspend fun getRecipesListByCategory(category: String): NetworkRecipeResult =
        client.get("${baseUrl}filter.php") { parameter("c", category) }.body()

    // Filter by Area
    suspend fun getRecipesListByArea(area: String): NetworkRecipeResult =
        client.get("${baseUrl}filter.php") { parameter("a", area) }.body()
}
