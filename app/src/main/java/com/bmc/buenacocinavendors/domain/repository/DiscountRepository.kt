package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.dto.CreateDiscountDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateDiscountDto
import com.bmc.buenacocinavendors.data.network.service.DiscountService
import com.bmc.buenacocinavendors.data.paging.DiscountPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiscountRepository @Inject constructor(
    private val discountService: DiscountService,
    private val firestore: FirebaseFirestore
) {
    fun create(
        dto: CreateDiscountDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        discountService.create(dto, onSuccess, onFailure)
    }

    fun update(
        id: String,
        dto: UpdateDiscountDto,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        discountService.update(id, dto, onSuccess, onFailure)
    }

    fun delete(
        id: String,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        discountService.delete(id, onSuccess, onFailure)
    }

    fun get(id: String): Flow<DiscountDomain?> {
        val discount = discountService.get(id)
        return discount.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<DiscountDomain>> {
        val discounts = discountService.get(query)
        return discounts.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<DiscountDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
            ),
            pagingSourceFactory = { DiscountPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}