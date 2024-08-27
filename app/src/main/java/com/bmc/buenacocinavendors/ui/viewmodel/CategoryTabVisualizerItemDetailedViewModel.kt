package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = CategoryTabVisualizerItemDetailedViewModel.CategoryTabVisualizerItemDetailedViewModelFactory::class)
class CategoryTabVisualizerItemDetailedViewModel @AssistedInject constructor(
    categoryRepository: CategoryRepository,
    productRepository: ProductRepository,
    @Assisted("categoryId") private val categoryId: String,
    @Assisted("storeId") private val storeId: String
) : ViewModel() {
    val category = categoryRepository.get(categoryId)
    private val _qProducts: (Query) -> Query = { query ->
        query.where(
            Filter.and(
                Filter.equalTo(FieldPath.of("category", "id"), categoryId),
                Filter.equalTo(FieldPath.of("store", "id"), storeId)
            )
        )
    }
    val products = productRepository.paging(_qProducts).cachedIn(viewModelScope)

    @AssistedFactory
    interface CategoryTabVisualizerItemDetailedViewModelFactory {
        fun create(
            @Assisted("categoryId") categoryId: String,
            @Assisted("storeId") storeId: String
        ): CategoryTabVisualizerItemDetailedViewModel
    }
}