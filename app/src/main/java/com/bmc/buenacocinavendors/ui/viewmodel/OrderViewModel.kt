package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.domain.model.StoreDomain
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun orders(): Flow<PagingData<OrderDomain>> = flow {
        emit(userRepository.getUserId())
    }.flatMapLatest { result ->
        when (result) {
            is Result.Error -> {
                flowOf(PagingData.empty())
            }

            is Result.Success -> {
                val store = getStore(result.data)
                if (store != null) {
                    val qOrders: (Query) -> Query = { query ->
                        query.whereEqualTo(FieldPath.of("store", "id"), store.id)
                    }
                    orderRepository.paging(qOrders)
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
}