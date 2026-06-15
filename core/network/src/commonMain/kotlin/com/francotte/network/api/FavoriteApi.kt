package com.francotte.network.api

import com.francotte.network.model.ImageUpload
import com.francotte.network.model.NetworkCustomRecipe
import com.francotte.network.utils.appendImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class FavoriteApi(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun getFavoriteRecipeIds(token: String): List<String> =
        client.get("${baseUrl}users/favorites") {
            header(HttpHeaders.Authorization, token)
        }.body()

    suspend fun getUserRecipes(token: String): List<NetworkCustomRecipe> =
        client.get("${baseUrl}users/recipes") {
            header(HttpHeaders.Authorization, token)
        }.body()

    suspend fun getUserRecipe(token: String, recipeId: String): NetworkCustomRecipe =
        client.get("${baseUrl}users/recipes/$recipeId") {
            header(HttpHeaders.Authorization, token)
        }.body()

    suspend fun getRecipeFavoriteStatus(recipeId: String, token: String): Boolean =
        client.get("${baseUrl}users/favorites/$recipeId/status") {
            header(HttpHeaders.Authorization, token)
        }.body()

    suspend fun addFavorite(recipeId: String, token: String) {
        client.post("${baseUrl}users/favorites/$recipeId") {
            header(HttpHeaders.Authorization, token)
        }
    }

    suspend fun removeFavorite(recipeId: String, token: String) {
        client.delete("${baseUrl}users/favorites/$recipeId") {
            header(HttpHeaders.Authorization, token)
        }
    }

    suspend fun addRecipe(
        token: String,
        image: ImageUpload?,
        title: String,
        instructions: String,
        ingredients: String,
    ): HttpResponse =
        client.post("${baseUrl}users/recipes") {
            header(HttpHeaders.Authorization, token)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("title", title)
                        append("instructions", instructions)
                        append("ingredients", ingredients)
                        image?.let { appendImage(it) }
                    },
                ),
            )
        }

    suspend fun updateRecipe(
        token: String,
        recipeId: String,
        image: ImageUpload?,
        title: String,
        instructions: String,
        ingredients: String,
    ): HttpResponse =
        client.put("${baseUrl}users/recipes/$recipeId") {
            header(HttpHeaders.Authorization, token)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("title", title)
                        append("instructions", instructions)
                        append("ingredients", ingredients)
                        image?.let { appendImage(it) }
                    },
                ),
            )
        }
}
