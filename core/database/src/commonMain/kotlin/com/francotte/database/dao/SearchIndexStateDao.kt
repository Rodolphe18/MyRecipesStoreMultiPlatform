package com.francotte.database.dao

import com.francotte.database.model.SearchIndexCategoryStateEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchIndexStateDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.searchIndexStateQueries

    suspend fun getCategoriesToIndex(staleBefore: Long, limit: Int): List<String> = withContext(dispatcher) {
        q.getCategoriesToIndex(staleBefore, limit.toLong()).executeAsList()
    }

    suspend fun getRemainingToIndexCount(staleBefore: Long): Int = withContext(dispatcher) {
        q.getRemainingToIndexCount(staleBefore).executeAsOne().toInt()
    }

    suspend fun upsertStates(states: List<SearchIndexCategoryStateEntity>) = withContext(dispatcher) {
        db.transaction {
            states.forEach { q.upsertState(it.strCategory, it.lastIndexedAt) }
        }
    }
}
