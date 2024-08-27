package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = DiscountTabVisualizerItemDetailedViewModel.DiscountTabVisualizerItemDetailedViewModelFactory::class)
class DiscountTabVisualizerItemDetailedViewModel @AssistedInject constructor(
    discountRepository: DiscountRepository,
    productRepository: ProductRepository,
    @Assisted("discountId") val discountId: String,
    @Assisted("storeId") val storeId: String
) : ViewModel() {
    val discount = discountRepository.get(discountId)
    private val _qProducts: (Query) -> Query = { query ->
        query.where(
            Filter.and(
                Filter.equalTo(FieldPath.of("discount", "id"), discountId),
                Filter.equalTo(FieldPath.of("store", "id"), storeId)
            )
        )
    }
    val products = productRepository.paging(_qProducts).cachedIn(viewModelScope)

    @AssistedFactory
    interface DiscountTabVisualizerItemDetailedViewModelFactory {
        fun create(
            @Assisted("discountId") discountId: String,
            @Assisted("storeId") storeId: String
        ): DiscountTabVisualizerItemDetailedViewModel
    }
}