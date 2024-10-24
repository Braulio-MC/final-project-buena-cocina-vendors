package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.service.StoreReviewService
import com.bmc.buenacocinavendors.data.paging.StoreReviewPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.StoreReviewDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreReviewRepository @Inject constructor(
    private val storeReviewService: StoreReviewService,
    private val firestore: FirebaseFirestore
) {
    fun get(id: String): Flow<StoreReviewDomain?> {
        val review = storeReviewService.get(id)
        return review.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<StoreReviewDomain>> {
        val reviews = storeReviewService.get(query)
        return reviews.map { list -> list.map { it.asDomain() } }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<StoreReviewDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE
            ),
            pagingSourceFactory = { StoreReviewPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}