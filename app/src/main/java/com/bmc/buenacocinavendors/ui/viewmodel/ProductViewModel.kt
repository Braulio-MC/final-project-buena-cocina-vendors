package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ProductViewModel.ProductViewModelFactory::class)
class ProductViewModel @AssistedInject constructor(
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository,
    discountRepository: DiscountRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _qProducts: (Query) -> Query = { query ->
        query.whereEqualTo(FieldPath.of("store", "id"), storeId)
    }
    private val _qMyCategories: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", storeId)
    }
    private val _qGeneralCategories: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", "")
    }
    private val _qDiscounts: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", storeId)
    }
    private val _qDefaultDiscount: (Query) -> Query = { query ->
        query.whereEqualTo("name", "default")
        query.whereEqualTo("storeId", "")
            .limit(1)
    }
    val products = productRepository
        .paging(_qProducts)
        .cachedIn(viewModelScope)
    val categories = categoryRepository
        .paging(_qMyCategories)
        .cachedIn(viewModelScope)
    val generalCategories = categoryRepository
        .paging(_qGeneralCategories)
        .cachedIn(viewModelScope)
    val discounts = discountRepository
        .paging(_qDiscounts)
        .cachedIn(viewModelScope)
    val defaultDiscount = discountRepository
        .get(_qDefaultDiscount)

    @AssistedFactory
    interface ProductViewModelFactory {
        fun create(storeId: String): ProductViewModel
    }
}