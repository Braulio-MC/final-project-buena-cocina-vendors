package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.dto.UpdateOrderDto
import com.bmc.buenacocinavendors.data.network.service.OrderService
import com.bmc.buenacocinavendors.data.paging.OrderPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService,
    private val firestore: FirebaseFirestore
) {
    fun update(
        id: String,
        dto: UpdateOrderDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        orderService.update(id, dto, onSuccess, onFailure)
    }

    fun get(id: String): Flow<OrderDomain?> {
        val order = orderService.get(id)
        return order.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<OrderDomain>> {
        val orders = orderService.get(query)
        return orders.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<OrderDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
            ),
            pagingSourceFactory = { OrderPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}