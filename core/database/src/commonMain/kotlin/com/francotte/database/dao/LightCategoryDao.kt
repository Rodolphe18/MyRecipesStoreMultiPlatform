package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.francotte.database.model.LightCategoryEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LightCategoryDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.categoryQueries

    fun getAllLightCategories(): Flow<List<LightCategoryEntity>> =
        q.getAllLightCategories().asFlow().mapToList(dispatcher).map { rows -> rows.map { LightCategoryEntity(it) } }

    suspend fun getLightCategoryByName(categoryName: String): LightCategoryEntity? = withContext(dispatcher) {
        q.getLightCategoryByName(categoryName).executeAsOneOrNull()?.let { LightCategoryEntity(it) }
    }

    suspend fun upsertAllLightCategories(categories: List<LightCategoryEntity>) = withContext(dispatcher) {
        db.transaction {
            categories.forEach { q.insertLightCategoryReplace(it.strCategory) }
        }
    }

    suspend fun insertLightCategory(category: LightCategoryEntity) = withContext(dispatcher) {
        q.insertLightCategoryReplace(category.strCategory)
    }

    suspend fun clearAll() = withContext(dispatcher) { q.clearLightCategories() }
}
