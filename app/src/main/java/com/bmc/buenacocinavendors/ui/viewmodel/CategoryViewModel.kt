package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = CategoryViewModel.CategoryViewModelFactory::class)
class CategoryViewModel @AssistedInject constructor(
    categoryRepository: CategoryRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _qMyCategories: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", storeId)
    }
    private val _qGeneralCategories: (Query) -> Query = { query ->
        query.whereEqualTo("storeId", "")
    }
    val categories = categoryRepository
        .paging(_qMyCategories)
        .cachedIn(viewModelScope)
    val generalCategories = categoryRepository
        .paging(_qGeneralCategories)
        .cachedIn(viewModelScope)

    @AssistedFactory
    interface CategoryViewModelFactory {
        fun create(storeId: String): CategoryViewModel
    }
}