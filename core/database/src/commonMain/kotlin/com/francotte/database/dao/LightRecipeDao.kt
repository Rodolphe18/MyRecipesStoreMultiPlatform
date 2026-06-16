package com.francotte.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.francotte.database.crossrefs.AreaWithRecipes
import com.francotte.database.crossrefs.CategoryWithRecipes
import com.francotte.database.crossrefs.IngredientWithRecipes
import com.francotte.database.crossrefs.RecipeAreaCrossRef
import com.francotte.database.crossrefs.RecipeCategoryCrossRef
import com.francotte.database.crossrefs.RecipeIngredientCrossRef
import com.francotte.database.internal.toEntity
import com.francotte.database.model.AreaEntity
import com.francotte.database.model.IngredientEntity
import com.francotte.database.model.LightCategoryEntity
import com.francotte.database.model.LightRecipeEntity
import com.francotte.database.sql.FoodDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LightRecipeDao(
    private val db: FoodDb,
    private val dispatcher: CoroutineDispatcher,
) {
    private val lr get() = db.lightRecipeQueries
    private val xref get() = db.crossRefQueries
    private val areas get() = db.areaQueries
    private val cats get() = db.categoryQueries

    // ------- AREA ---------
    fun observeAreaWithRecipes(area: String): Flow<AreaWithRecipes?> =
        xref.recipesForArea(area).asFlow().mapToList(dispatcher)
            .map { rows -> AreaWithRecipes(AreaEntity(area), rows.map { it.toEntity() }) }

    suspend fun upsertArea(area: AreaEntity) = withContext(dispatcher) {
        areas.insertArea(area.strArea, area.savedTimeStamp)
    }

    suspend fun getLastUpdatedForArea(area: String): Long? = withContext(dispatcher) {
        xref.getLastUpdatedForArea(area).executeAsOne().lastUpdated
    }

    suspend fun clearAreaRefs(area: String) = withContext(dispatcher) { xref.clearAreaRefs(area) }

    suspend fun upsertAreaRefs(refs: List<RecipeAreaCrossRef>) = withContext(dispatcher) {
        db.transaction { refs.forEach { xref.upsertAreaRef(it.strArea, it.idMeal, it.savedTimestamp) } }
    }

    suspend fun upsertAreaWithRecipes(
        area: String,
        recipes: List<LightRecipeEntity>,
        refs: List<RecipeAreaCrossRef>,
        clearBefore: Boolean,
    ) = withContext(dispatcher) {
        db.transaction {
            areas.insertArea(area, null)
            recipes.forEach { lr.insertLightRecipe(it.idMeal, it.strMeal, it.strMealThumb) }
            if (clearBefore) xref.clearAreaRefs(area)
            refs.forEach { xref.upsertAreaRef(it.strArea, it.idMeal, it.savedTimestamp) }
        }
    }

    // ------- CATEGORY ---------
    fun observeCategoryWithRecipes(category: String): Flow<CategoryWithRecipes?> =
        xref.recipesForCategory(category).asFlow().mapToList(dispatcher)
            .map { rows -> CategoryWithRecipes(LightCategoryEntity(category), rows.map { it.toEntity() }) }

    suspend fun upsertCategory(category: LightCategoryEntity): Long = withContext(dispatcher) {
        cats.insertLightCategoryIgnore(category.strCategory)
        1L
    }

    suspend fun upsertCategoryRefs(refs: List<RecipeCategoryCrossRef>) = withContext(dispatcher) {
        db.transaction { refs.forEach { xref.upsertCategoryRef(it.strCategory, it.idMeal, it.savedTimestamp) } }
    }

    suspend fun clearCategoryRefs(category: String) = withContext(dispatcher) { xref.clearCategoryRefs(category) }

    suspend fun upsertCategoryWithRecipes(
        category: String,
        recipes: List<LightRecipeEntity>,
        refs: List<RecipeCategoryCrossRef>,
        clearBefore: Boolean,
    ): Boolean = withContext(dispatcher) {
        db.transaction {
            cats.insertLightCategoryIgnore(category)
            recipes.forEach { lr.insertLightRecipe(it.idMeal, it.strMeal, it.strMealThumb) }
            if (clearBefore) xref.clearCategoryRefs(category)
            refs.forEach { xref.upsertCategoryRef(it.strCategory, it.idMeal, it.savedTimestamp) }
        }
        true
    }

    // ----- INGREDIENTS ---------
    fun observeIngredientWithRecipes(ingredientName: String): Flow<IngredientWithRecipes> =
        xref.recipesForIngredient(ingredientName).asFlow().mapToList(dispatcher)
            .map { rows ->
                IngredientWithRecipes(
                    IngredientEntity(name = ingredientName, description = "", imageUrl = ""),
                    rows.map { it.toEntity() },
                )
            }

    suspend fun getLastUpdatedForIngredientRecipes(ingredientName: String): Long? = withContext(dispatcher) {
        xref.getLastUpdatedForIngredientRecipes(ingredientName).executeAsOne().lastUpdated
    }

    suspend fun clearIngredientRefs(ingredientName: String) = withContext(dispatcher) {
        xref.clearIngredientRefs(ingredientName)
    }

    suspend fun upsertRecipes(recipes: List<LightRecipeEntity>) = upsertLightRecipes(recipes)

    suspend fun upsertIngredientRefs(refs: List<RecipeIngredientCrossRef>) = withContext(dispatcher) {
        db.transaction { refs.forEach { xref.upsertIngredientRef(it.ingredientName, it.idMeal, it.savedTimestamp) } }
    }

    suspend fun upsertIngredientWithRecipes(
        ingredientName: String,
        recipes: List<LightRecipeEntity>,
        refs: List<RecipeIngredientCrossRef>,
        clearBefore: Boolean,
    ) = withContext(dispatcher) {
        db.transaction {
            recipes.forEach { lr.insertLightRecipe(it.idMeal, it.strMeal, it.strMealThumb) }
            if (clearBefore) xref.clearIngredientRefs(ingredientName)
            refs.forEach { xref.upsertIngredientRef(it.ingredientName, it.idMeal, it.savedTimestamp) }
        }
    }

    // --- RECIPES ---
    suspend fun upsertLightRecipes(recipes: List<LightRecipeEntity>) = withContext(dispatcher) {
        db.transaction { recipes.forEach { lr.insertLightRecipe(it.idMeal, it.strMeal, it.strMealThumb) } }
    }

    suspend fun getLastUpdatedForCategory(category: String): Long? = withContext(dispatcher) {
        xref.getLastUpdatedForCategory(category).executeAsOne().lastUpdated
    }

    suspend fun getLightRecipeById(id: String): LightRecipeEntity? = withContext(dispatcher) {
        lr.getLightRecipeById(id).executeAsOneOrNull()?.toEntity()
    }

    suspend fun upsertAllLightRecipes(recipes: List<LightRecipeEntity>) = upsertLightRecipes(recipes)

    suspend fun insertLightRecipe(recipe: LightRecipeEntity) = withContext(dispatcher) {
        lr.insertLightRecipe(recipe.idMeal, recipe.strMeal, recipe.strMealThumb)
    }

    suspend fun clearAll() = withContext(dispatcher) { lr.clearLightRecipes() }

    fun observeByIds(useFilterIds: Boolean, filterIds: Set<String>): Flow<List<LightRecipeEntity>> =
        if (!useFilterIds || filterIds.isEmpty()) {
            flowOf(emptyList())
        } else {
            lr.selectByIds(filterIds).asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }
        }

    suspend fun getAllOnce(): List<LightRecipeEntity> = withContext(dispatcher) {
        lr.getAllLightRecipes().executeAsList().map { it.toEntity() }
    }

    // ---- SEARCH (LIKE-based, multiplatform) ----
    fun searchRecipesByName(query: String, limit: Int): Flow<List<LightRecipeEntity>> =
        lr.searchRecipesByName(query, limit.toLong()).asFlow().mapToList(dispatcher).map { rows -> rows.map { it.toEntity() } }

    fun observeRecipeCount(): Flow<Int> =
        lr.countLightRecipes().asFlow().mapToOne(dispatcher).map { it.toInt() }
}
