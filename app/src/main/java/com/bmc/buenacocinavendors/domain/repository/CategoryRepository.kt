package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.dto.CreateCategoryDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateCategoryDto
import com.bmc.buenacocinavendors.data.network.service.CategoryService
import com.bmc.buenacocinavendors.data.paging.CategoryPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryService: CategoryService,
    private val firestore: FirebaseFirestore
) {
    fun create(
        dto: CreateCategoryDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        categoryService.create(dto, onSuccess, onFailure)
    }

    fun update(
        id: String,
        dto: UpdateCategoryDto,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        categoryService.update(id, dto, onSuccess, onFailure)
    }

    fun delete(
        id: String,
        name: String,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        categoryService.delete(id, name, onSuccess, onFailure)
    }

    fun get(id: String): Flow<CategoryDomain?> {
        val category = categoryService.get(id)
        return category.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<CategoryDomain>> {
        val categories = categoryService.get(query)
        return categories.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<CategoryDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
            ),
            pagingSourceFactory = { CategoryPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}