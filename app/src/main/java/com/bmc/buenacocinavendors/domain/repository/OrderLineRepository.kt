package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.service.OrderLineService
import com.bmc.buenacocinavendors.data.paging.OrderLinePagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderLineRepository @Inject constructor(
    private val orderLineService: OrderLineService,
    private val firestore: FirebaseFirestore
) {
    fun get(orderId: String, orderLineId: String): Flow<OrderLineDomain?> {
        val orderLine = orderLineService.get(orderId, orderLineId)
        return orderLine.map { it?.asDomain() }
    }

    fun get(orderId: String): Flow<List<OrderLineDomain>> {
        val orderLines = orderLineService.get(orderId)
        return orderLines.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<OrderLineDomain>> {
        val orderLines = orderLineService.get(query)
        return orderLines.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<OrderLineDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
            ),
            pagingSourceFactory = { OrderLinePagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}