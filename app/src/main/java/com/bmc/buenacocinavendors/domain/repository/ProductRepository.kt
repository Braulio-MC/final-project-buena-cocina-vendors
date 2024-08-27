package com.bmc.buenacocinavendors.domain.repository

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.DOC_DEFAULT_TYPE
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.core.uriToBase64
import com.bmc.buenacocinavendors.data.network.dto.CreateProductDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateProductDto
import com.bmc.buenacocinavendors.data.network.service.ProductService
import com.bmc.buenacocinavendors.data.paging.ProductPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val appContext: Context
) {
    fun create(
        dto: CreateProductDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val doc = DocumentFile.fromSingleUri(appContext, dto.image)
        doc?.let { document ->
            val docType = (document.type ?: DOC_DEFAULT_TYPE)
            val imageBase64 = uriToBase64(appContext.contentResolver, dto.image)
            val fileName = document.name ?: ""
            if (imageBase64 == null) {
                onFailure(Exception("Uri to base64 failed")) // Custom exception here
            } else {
                productService.create(
                    dto,
                    imageBase64,
                    fileName,
                    docType,
                    onSuccess,
                    onFailure
                )
            }
        }
    }

    fun update(
        productId: String,
        dto: UpdateProductDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (dto.image != null && dto.oldPath != null) {
            val doc = DocumentFile.fromSingleUri(appContext, dto.image)
            doc?.let { document ->
                val docType = (document.type ?: DOC_DEFAULT_TYPE)
                val imageBase64 = uriToBase64(appContext.contentResolver, dto.image)
                val fileName = document.name ?: ""
                if (imageBase64 == null) {
                    onFailure(Exception("Uri to base64 failed")) // Custom exception here
                } else {
                    productService.update(
                        productId,
                        dto,
                        imageBase64,
                        fileName,
                        docType,
                        onSuccess,
                        onFailure
                    )
                }
            }
        } else {
            productService.update(
                productId,
                dto,
                null,
                null,
                null,
                onSuccess,
                onFailure
            )
        }
    }

    fun delete(
        productId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        productService.delete(productId, onSuccess, onFailure)
    }

    fun get(id: String): Flow<ProductDomain?> {
        val product = productService.get(id)
        return product.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<ProductDomain>> {
        val products = productService.get(query)
        return products.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<ProductDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE
            ),
            pagingSourceFactory = { ProductPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}