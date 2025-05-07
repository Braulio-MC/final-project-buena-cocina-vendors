package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.visualizer.detailed.CategoryTabVisualizerItemDetailedUiState
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = CategoryTabVisualizerItemDetailedViewModel.CategoryTabVisualizerItemDetailedViewModelFactory::class)
class CategoryTabVisualizerItemDetailedViewModel @AssistedInject constructor(
    categoryRepository: CategoryRepository,
    productRepository: ProductRepository,
    @Assisted("categoryId") private val categoryId: String,
    @Assisted("categoryName") private val categoryName: String,
    @Assisted("storeId") private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryTabVisualizerItemDetailedUiState())
    val uiState = _uiState
        .onStart {
            categoryRepository.get(categoryId)
                .onStart {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }
                .onEach { category ->
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, category = category)
                    }
                }
                .launchIn(viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = CategoryTabVisualizerItemDetailedUiState()
        )
    private val _qProducts: (Query) -> Query = { query ->
        val targetCategory = mapOf("id" to categoryId, "name" to categoryName)
        query.where(
            Filter.and(
                Filter.arrayContains("categories", targetCategory),
                Filter.equalTo(FieldPath.of("store", "id"), storeId)
            )
        )
    }
    val products = productRepository.paging(_qProducts).cachedIn(viewModelScope)

    @AssistedFactory
    interface CategoryTabVisualizerItemDetailedViewModelFactory {
        fun create(
            @Assisted("categoryId") categoryId: String,
            @Assisted("categoryName") categoryName: String,
            @Assisted("storeId") storeId: String
        ): CategoryTabVisualizerItemDetailedViewModel
    }
}