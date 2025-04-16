package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchParamsObject
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_INDEX_HITS_PER_PAGE
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_MULTI_INDEX_HITS_PER_PAGE
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_PAGING_HITS_PER_PAGE
import com.bmc.buenacocinavendors.core.Searchable
import com.bmc.buenacocinavendors.data.network.service.SearchService
import com.bmc.buenacocinavendors.data.paging.SearchPagingSource
import com.bmc.buenacocinavendors.di.AlgoliaClientFactory
import com.bmc.buenacocinavendors.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: SearchService,
    private val algoliaClient: SearchClient,
    private val algoliaClientFactory: AlgoliaClientFactory
) {
    fun searchMultiIndex(
        query: String,
        indexNames: List<String>,
        hitsPerPage: Int = ALGOLIA_SEARCH_MULTI_INDEX_HITS_PER_PAGE
    ): Flow<List<Searchable>> {
        val response = service.searchMultiIndex(query, indexNames, hitsPerPage)
        return response.map { list ->
            list.map { searchable -> searchable.toDomain() }
        }
    }

    fun searchIndex(
        query: String,
        indexName: String,
        hitsPerPage: Int = ALGOLIA_SEARCH_INDEX_HITS_PER_PAGE
    ): Flow<List<Searchable>> {
        val response = service.searchIndex(query, indexName, hitsPerPage)
        return response.map { list ->
            list.map { searchable -> searchable.toDomain() }
        }
    }

    fun searchIndexWithScopedApiKey(
        query: String,
        indexName: String,
        hitsPerPage: Int = ALGOLIA_SEARCH_INDEX_HITS_PER_PAGE,
        scopedSecuredApiKey: String
    ): Flow<List<Searchable>> {
        val response =
            service.searchIndexWithScopedApiKey(query, indexName, hitsPerPage, scopedSecuredApiKey)
        return response.map { list ->
            list.map { searchable -> searchable.toDomain() }
        }
    }

    fun paging(
        query: String,
        indexName: String,
        filters: String? = null
    ): Flow<PagingData<Searchable>> {
        return Pager(
            config = PagingConfig(
                pageSize = ALGOLIA_SEARCH_PAGING_HITS_PER_PAGE
            ),
            pagingSourceFactory = {
                val paramsObject = SearchParamsObject(
                    query = query,
                    filters = filters
                )
                SearchPagingSource(paramsObject, indexName, algoliaClient)
            }
        ).flow.map { pagingData ->
            pagingData.map { searchable -> searchable.toDomain() }
        }
    }

    fun pagingWithScopedApiKey(
        query: String,
        indexName: String,
        filters: String? = null,
        scopedSecuredApiKey: String
    ): Flow<PagingData<Searchable>> {
        val client = algoliaClientFactory.create(scopedSecuredApiKey)
        return Pager(
            config = PagingConfig(
                pageSize = ALGOLIA_SEARCH_PAGING_HITS_PER_PAGE
            ),
            pagingSourceFactory = {
                val paramsObject = SearchParamsObject(
                    query = query,
                    filters = filters
                )
                SearchPagingSource(paramsObject, indexName, client)
            }
        ).flow.map { pagingData ->
            pagingData.map { searchable -> searchable.toDomain() }
        }
    }
}