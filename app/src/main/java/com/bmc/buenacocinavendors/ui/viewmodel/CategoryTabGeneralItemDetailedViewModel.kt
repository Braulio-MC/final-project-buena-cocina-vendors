package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.domain.model.StoreDomain
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@HiltViewModel(assistedFactory = CategoryTabGeneralItemDetailedViewModel.CategoryTabGeneralItemDetailedViewModelFactory::class)
class CategoryTabGeneralItemDetailedViewModel @AssistedInject constructor(
    categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val productRepository: ProductRepository,
    @Assisted private val categoryId: String
) : ViewModel() {
    val category = categoryRepository.get(categoryId)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun products(): Flow<PagingData<ProductDomain>> = flow {
        emit(userRepository.getUserId())
    }.flatMapLatest { result ->
        when (result) {
            is Result.Error -> {
                flowOf(PagingData.empty())
            }

            is Result.Success -> {
                val store = getStore(result.data)
                if (store != null) {
                    val qProducts: (Query) -> Query = { query ->
                        query.where(
                            Filter.and(
                                Filter.equalTo(FieldPath.of("category", "id"), categoryId),
                                Filter.equalTo(FieldPath.of("store", "id"), store.id)
                            )
                        )
                    }
                    productRepository.paging(qProducts)
                } else {
                    flowOf(PagingData.empty())
                }
            }
        }
    }.cachedIn(viewModelScope)

    private suspend fun getStore(userId: String): StoreDomain? {
        val qStore: (Query) -> Query = { query ->
            query.whereEqualTo("userId", userId)
                .limit(1)
        }
        val first = storeRepository.get(qStore).firstOrNull()
        return first?.firstOrNull()
    }

    @AssistedFactory
    interface CategoryTabGeneralItemDetailedViewModelFactory {
        fun create(
            categoryId: String,
        ): CategoryTabGeneralItemDetailedViewModel
    }
}