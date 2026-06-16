package com.francotte.data.repository

import com.francotte.data.interfaces.SearchContentsRepository
import com.francotte.data.mapper.entity.asExternalModel
import com.francotte.database.dao.AreaDao
import com.francotte.database.dao.FullCategoryDao
import com.francotte.database.dao.IngredientDao
import com.francotte.database.dao.LightRecipeDao
import com.francotte.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Multiplatform search backed by SQL `LIKE` queries. Each source table is queried directly,
 * so there is no separate index to maintain; readiness simply means the recipe table has data.
 */
class DefaultSearchContentsRepository(
    private val lightRecipeDao: LightRecipeDao,
    private val categoriesDao: FullCategoryDao,
    private val areasDao: AreaDao,
    private val ingredientDao: IngredientDao,
) : SearchContentsRepository {

    override fun searchContentsIsReady(): Flow<Boolean> =
        lightRecipeDao.observeRecipeCount().map { it > 0 }

    override fun searchContents(searchQuery: String): Flow<SearchResult> {
        val q = searchQuery.trim()
        if (q.isEmpty()) return flowOf(SearchResult())

        return combine(
            lightRecipeDao.searchRecipesByName(q, limit = 100),
            categoriesDao.searchCategoryNames(q, limit = 20),
            areasDao.searchAreaNames(q, limit = 20),
            ingredientDao.searchIngredientNames(q, limit = 20),
        ) { recipes, cats, areas, ing ->
            SearchResult(
                categories = cats,
                areas = areas,
                ingredients = ing,
                lightRecipes = recipes.map { it.asExternalModel() },
            )
        }
    }
}
