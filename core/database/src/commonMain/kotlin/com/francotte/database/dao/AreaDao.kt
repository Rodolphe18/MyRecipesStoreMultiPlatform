package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.francotte.database.internal.toEntity
import com.francotte.database.model.AreaEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AreaDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val q get() = db.areaQueries

    suspend fun insertAllAreas(areas: List<AreaEntity>): List<Long> = withContext(dispatcher) {
        db.transaction {
            areas.forEach { q.insertArea(it.strArea, it.savedTimeStamp) }
        }
        List(areas.size) { 1L }
    }

    suspend fun getAllAreasOnce(): List<AreaEntity> = withContext(dispatcher) {
        q.getAllAreasOnce().executeAsList().map { it.toEntity() }
    }

    fun observeALlAreas(): Flow<List<AreaEntity>> =
        q.observeAllAreas().asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    fun searchAreaNames(query: String, limit: Int): Flow<List<String>> =
        q.searchAreaNames(query, limit.toLong()).asFlow().mapToList(dispatcher)
}
