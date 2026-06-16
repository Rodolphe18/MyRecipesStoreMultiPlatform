package com.francotte.data.repository

import com.francotte.data.interfaces.OfflineFirstFullRecipeRepository
import com.francotte.data.mapper.dto.asEntity
import com.francotte.data.mapper.entity.asExternalModel
import com.francotte.database.dao.FullRecipeDao
import com.francotte.model.Recipe
import com.francotte.network.api.RecipeApi
import com.francotte.network.model.NetworkRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class OfflineFirstFullRecipeRepositoryImpl(
    private val api: RecipeApi,
    private val dao: FullRecipeDao,
) : OfflineFirstFullRecipeRepository {

    override fun getRecipeDetail(id: Long): Flow<Recipe> =
        flow {
            val localRecipe = dao.getFullRecipeById(id.toString()).first()
            val lastUpdated = localRecipe?.savedTimestamp
            val now = Clock.System.now().toEpochMilliseconds()
            val timeToLive = 3.days
            if (localRecipe == null || lastUpdated == null || lastUpdated < now - timeToLive.inWholeMilliseconds) {
                try {
                    val networkRecipe = api
                        .getMealDetail(id)
                        .meals
                        .filterIsInstance<NetworkRecipe>()
                        .firstOrNull()
                    if (networkRecipe != null) {
                        val entity = networkRecipe.asEntity().apply {
                            this.savedTimestamp = now
                        }
                        dao.insertFullRecipe(entity)
                    }
                } catch (e: Exception) {
                    // Optional: log or handle network errors
                }
            }
            val finalRecipe = dao.getFullRecipeById(id.toString()).first()
            if (finalRecipe != null) {
                emit(finalRecipe.asExternalModel())
            }
        }
}
