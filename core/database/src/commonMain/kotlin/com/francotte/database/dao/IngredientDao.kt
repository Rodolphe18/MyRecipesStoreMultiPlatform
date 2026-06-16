package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.francotte.database.internal.toEntity
import com.francotte.database.model.IngredientEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class IngredientDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.ingredientQueries

    suspend fun insertIngredient(ingredient: IngredientEntity) = withContext(dispatcher) {
        q.insertIngredient(ingredient.name, ingredient.description, ingredient.imageUrl, ingredient.savedTimeStamp)
    }

    suspend fun insertIngredients(ingredients: List<IngredientEntity>): List<Long> = withContext(dispatcher) {
        db.transaction {
            ingredients.forEach {
                q.insertIngredient(it.name, it.description, it.imageUrl, it.savedTimeStamp)
            }
        }
        List(ingredients.size) { 1L }
    }

    suspend fun getLastUpdatedForIngredients(): Long? = withContext(dispatcher) {
        q.getLastUpdatedForIngredients().executeAsOne().lastUpdated
    }

    fun observeIngredients(): Flow<List<IngredientEntity>> =
        q.observeIngredients().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    suspend fun getAllIngredientsOnce(): List<IngredientEntity> = withContext(dispatcher) {
        q.getAllIngredientsOnce().executeAsList().map { it.toEntity() }
    }

    suspend fun clearAllIngredients(ingredients: List<IngredientEntity>) = withContext(dispatcher) {
        db.transaction {
            ingredients.forEach { q.deleteIngredientByName(it.name) }
        }
    }

    fun searchIngredientNames(query: String, limit: Int): Flow<List<String>> =
        q.searchIngredientNames(query, limit.toLong()).asFlow().mapToList(dispatcher)
}
