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
import com.bmc.buenacocinavendors.data.network.dto.CreateStoreDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateStoreDto
import com.bmc.buenacocinavendors.data.network.service.StoreService
import com.bmc.buenacocinavendors.data.paging.StorePagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.StoreDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val storeService: StoreService,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val appContext: Context
) {
    fun create(
        dto: CreateStoreDto,
        onSuccess: () -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val doc = DocumentFile.fromSingleUri(appContext, dto.image)
        doc?.let { document ->
            val docType = (document.type ?: DOC_DEFAULT_TYPE)
            val imageBase64 = uriToBase64(appContext.contentResolver, dto.image)
            val fileName = document.name ?: ""
            if (imageBase64 == null) {
                onFailure("Uri to base64 failed", "")
            } else {
                storeService.create(
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
        storeId: String,
        dto: UpdateStoreDto,
        onSuccess: () -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        if (dto.image != null && dto.oldPath != null) {
            val doc = DocumentFile.fromSingleUri(appContext, dto.image)
            doc?.let { document ->
                val docType = (document.type ?: DOC_DEFAULT_TYPE)
                val imageBase64 = uriToBase64(appContext.contentResolver, dto.image)
                val fileName = document.name ?: ""
                if (imageBase64 == null) {
                    onFailure("Uri to base64 failed", "")
                } else {
                    storeService.update(
                        storeId,
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
            storeService.update(
                storeId,
                dto,
                null,
                null,
                null,
                onSuccess,
                onFailure
            )
        }
    }

    fun get(storeId: String): Flow<StoreDomain?> {
        val store = storeService.get(storeId)
        return store.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<StoreDomain>> {
        val stores = storeService.get(query)
        return stores.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<StoreDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE
            ),
            pagingSourceFactory = { StorePagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}