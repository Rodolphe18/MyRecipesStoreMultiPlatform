package com.francotte.data.interfaces

import com.francotte.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchContentsRepository {
    fun searchContents(searchQuery: String): Flow<SearchResult>
    fun searchContentsIsReady(): Flow<Boolean>
}
