package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.francotte.database.internal.toEntity
import com.francotte.database.internal.toRow
import com.francotte.database.model.FullRecipeEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FullRecipeDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.fullRecipeQueries

    fun getAllFullRecipes(): Flow<List<FullRecipeEntity>> =
        q.getAllFullRecipes().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    fun getLatestFullRecipes(): Flow<List<FullRecipeEntity>> =
        q.getLatestFullRecipes().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    fun getAllFavoritesFullRecipes(): Flow<List<FullRecipeEntity>> =
        q.getAllFavoritesFullRecipes().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    fun getFullRecipeById(id: String): Flow<FullRecipeEntity?> =
        q.getFullRecipeById(id).asFlow().mapToOneOrNull(dispatcher).map { it?.toEntity() }

    suspend fun getExistingIds(ids: List<String>): List<String> = withContext(dispatcher) {
        q.getExistingIds(ids).executeAsList()
    }

    fun observeFullRecipesByIds(ids: List<String>): Flow<List<FullRecipeEntity>> =
        q.selectByIds(ids).asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    suspend fun getFullRecipesByIdsSnapshot(ids: List<String>): List<FullRecipeEntity> = withContext(dispatcher) {
        q.selectByIds(ids).executeAsList().map { it.toEntity() }
    }

    suspend fun insertFullRecipe(recipe: FullRecipeEntity) = withContext(dispatcher) {
        q.insertFullRecipe(recipe.toRow())
    }

    suspend fun insertAllFullRecipes(recipes: List<FullRecipeEntity>) = withContext(dispatcher) {
        db.transaction { recipes.forEach { q.insertFullRecipe(it.toRow()) } }
    }

    suspend fun upsertAllFullRecipes(recipes: List<FullRecipeEntity>) = insertAllFullRecipes(recipes)

    suspend fun getLastUpdatedForLatest(): Long? = withContext(dispatcher) {
        q.getLastUpdatedForLatest().executeAsOne().lastUpdated
    }

    suspend fun deleteOldLatestRecipes() = withContext(dispatcher) { q.deleteOldLatestRecipes() }

    suspend fun deleteAllFavoritesRecipes() = withContext(dispatcher) { q.deleteAllFavoritesRecipes() }

    suspend fun clearAll() = withContext(dispatcher) { q.clearFullRecipes() }

    suspend fun refreshLatest(recipes: List<FullRecipeEntity>) = withContext(dispatcher) {
        db.transaction {
            q.deleteOldLatestRecipes()
            recipes.forEach { q.insertFullRecipe(it.toRow()) }
        }
    }
}
