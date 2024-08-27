package com.bmc.buenacocinavendors.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bmc.buenacocinavendors.core.PAGING_PAGE_SIZE
import com.bmc.buenacocinavendors.data.network.dto.CreateLocationDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateLocationDto
import com.bmc.buenacocinavendors.data.network.service.LocationService
import com.bmc.buenacocinavendors.data.paging.LocationPagingSource
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationService: LocationService,
    private val firestore: FirebaseFirestore
) {
    fun create(
        dto: CreateLocationDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        locationService.create(dto, onSuccess, onFailure)
    }

    fun update(
        id: String,
        dto: UpdateLocationDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        locationService.update(id, dto, onSuccess, onFailure)
    }

    fun delete(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        locationService.delete(id, onSuccess, onFailure)
    }

    fun get(id: String): Flow<LocationDomain?> {
        val location = locationService.get(id)
        return location.map { it?.asDomain() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<LocationDomain>> {
        val locations = locationService.get(query)
        return locations.map { list ->
            list.map { item ->
                item.asDomain()
            }
        }
    }

    fun paging(query: (Query) -> Query = { it }): Flow<PagingData<LocationDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
            ),
            pagingSourceFactory = { LocationPagingSource(query, firestore) }
        ).flow.map { pagingData ->
            pagingData.map { it.asDomain() }
        }
    }
}