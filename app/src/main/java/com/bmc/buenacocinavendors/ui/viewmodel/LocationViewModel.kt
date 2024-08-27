package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.LocationRepository
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = LocationViewModel.LocationViewModelFactory::class)
class LocationViewModel @AssistedInject constructor(
    locationRepository: LocationRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _qLocations: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", storeId)
    }
    val locations = locationRepository.paging(_qLocations).cachedIn(viewModelScope)

    @AssistedFactory
    interface LocationViewModelFactory {
        fun create(storeId: String): LocationViewModel
    }
}