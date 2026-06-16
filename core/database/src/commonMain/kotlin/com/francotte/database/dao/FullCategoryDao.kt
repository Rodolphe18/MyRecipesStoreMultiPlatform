package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.francotte.database.internal.toEntity
import com.francotte.database.model.CategoryEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FullCategoryDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.categoryQueries

    fun getAllCategories(): Flow<List<CategoryEntity>> =
        q.getAllCategories().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    suspend fun getAllCategoriesOnce(): List<CategoryEntity> = withContext(dispatcher) {
        q.getAllCategoriesOnce().executeAsList().map { it.toEntity() }
    }

    suspend fun getLightCategoryByName(categoryName: String): CategoryEntity? = withContext(dispatcher) {
        q.getFullCategoryByName(categoryName).executeAsOneOrNull()?.toEntity()
    }

    suspend fun getLastUpdateForCategories(): Long? = withContext(dispatcher) {
        q.getLastUpdateForCategories().executeAsOne().lastUpdated
    }

    suspend fun upsertAllCategories(categories: List<CategoryEntity>) = withContext(dispatcher) {
        db.transaction {
            categories.forEach {
                q.insertFullCategory(
                    it.idCategory, it.strCategory, it.strCategoryThumb, it.strCategoryDescription, it.savedTimestamp,
                )
            }
        }
    }

    suspend fun insertFullCategories(categories: List<CategoryEntity>) = upsertAllCategories(categories)

    suspend fun clearAll() = withContext(dispatcher) { q.clearFullCategories() }

    fun searchCategoryNames(query: String, limit: Int): Flow<List<String>> =
        q.searchCategoryNames(query, limit.toLong()).asFlow().mapToList(dispatcher)
}
