package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = DiscountViewModel.DiscountViewModelFactory::class)
class DiscountViewModel @AssistedInject constructor(
    discountRepository: DiscountRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _qDiscounts: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", storeId)
    }
    val discounts = discountRepository.paging(_qDiscounts).cachedIn(viewModelScope)

    @AssistedFactory
    interface DiscountViewModelFactory {
        fun create(storeId: String): DiscountViewModel
    }
}