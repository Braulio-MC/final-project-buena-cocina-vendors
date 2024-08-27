package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = StoreVisualizerViewModel.StoreVisualizerViewModelFactory::class)
class StoreVisualizerViewModel @AssistedInject constructor(
    storeRepository: StoreRepository,
    productRepository: ProductRepository,
    connectivityRepository: ConnectivityRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    val store = storeRepository.get(storeId)
    private val _qProducts: (Query) -> Query = { query ->
        query.whereEqualTo(FieldPath.of("store", "id"), storeId)
    }
    val products = productRepository.paging(_qProducts).cachedIn(viewModelScope)
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unavailable
        )

    @AssistedFactory
    interface StoreVisualizerViewModelFactory {
        fun create(storeId: String): StoreVisualizerViewModel
    }
}