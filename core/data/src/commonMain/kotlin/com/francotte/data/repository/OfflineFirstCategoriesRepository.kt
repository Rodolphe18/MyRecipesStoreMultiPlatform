package com.francotte.data.repository

import com.francotte.common.utils.DataResult
import com.francotte.common.utils.userMessage
import com.francotte.data.interfaces.CategoriesRepository
import com.francotte.data.mapper.dto.asEntity
import com.francotte.data.mapper.entity.asExternalModel
import com.francotte.database.dao.FullCategoryDao
import com.francotte.model.Category
import com.francotte.network.api.RecipeApi
import com.francotte.network.model.NetworkCategory
import com.francotte.network.utils.safeNetworkCall
import com.francotte.common.di.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class OfflineFirstCategoriesRepositoryImpl(
    private val api: RecipeApi,
    private val dao: FullCategoryDao,
) : CategoriesRepository {

    override fun observeAllMealCategories(): Flow<List<Category>> =
        dao.getAllCategories().map { categories -> categories.map { it.asExternalModel() } }

    override suspend fun refreshAllMealCategories(force: Boolean): String? {
        val lastUpdate = dao.getLastUpdateForCategories()
        val now = Clock.System.now().toEpochMilliseconds()
        val timeToLive = 7.days

        if (!force && lastUpdate != null) {
            val age = now - lastUpdate
            if (age < timeToLive.inWholeMilliseconds) return null
        }

        val networkCategories =
            safeNetworkCall(ioDispatcher) { api.getAllMealCategories().categories }

        val categories = when (networkCategories) {
            is DataResult.Failure -> return networkCategories.error.userMessage()
            is DataResult.Success -> networkCategories.data
        }
        val categoriesEntity = categories.map { (it as NetworkCategory).asEntity(now) }
        dao.upsertAllCategories(categoriesEntity)
        return "Synced successfully"
    }
}
