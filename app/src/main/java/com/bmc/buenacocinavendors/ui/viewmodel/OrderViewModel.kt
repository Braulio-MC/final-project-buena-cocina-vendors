package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_ORDERS_INDEX
import com.bmc.buenacocinavendors.core.AlgoliaGetSecuredSearchApiKeyScopes
import com.bmc.buenacocinavendors.core.OrderSearchChips
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.core.Searchable
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.domain.model.StoreDomain
import com.bmc.buenacocinavendors.domain.repository.AlgoliaTokenRepository
import com.bmc.buenacocinavendors.domain.repository.SearchRepository
import com.bmc.buenacocinavendors.ui.screen.order.OrderIntent
import com.bmc.buenacocinavendors.ui.screen.order.OrderUiState
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val searchRepository: SearchRepository,
    private val algoliaTokenRepository: AlgoliaTokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState
    private val _orderHits = MutableStateFlow<Flow<PagingData<Searchable>>?>(null)
    val orderHits = _orderHits.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val orders: Flow<PagingData<OrderDomain>> = flow {
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = PagingData.empty()
        )

    private suspend fun getStore(userId: String): StoreDomain? {
        val qStore: (Query) -> Query = { query ->
            query.whereEqualTo("userId", userId)
                .limit(1)
        }
        val first = storeRepository.get(qStore).firstOrNull()
        return first?.firstOrNull()
    }

    fun onIntent(intent: OrderIntent) {
        when (intent) {
            OrderIntent.Search -> {
                search()
            }

            is OrderIntent.UpdateSearchQuery -> {
                _uiState.update { currentState ->
                    currentState.copy(searchQuery = intent.searchQuery)
                }
            }

            OrderIntent.ClearSearch -> {
                _uiState.update { currentState ->
                    currentState.copy(searchQuery = "")
                }
                _orderHits.value = null
            }
        }
    }

    private fun search() {
        viewModelScope.launch {
            when (val rAccessToken = userRepository.getAccessToken()) {
                is Result.Error -> { }

                is Result.Success -> {
                    val authorization = "Bearer ${rAccessToken.data}"
                    when (val rScopedKey =
                        algoliaTokenRepository.requestScopedToken(
                            authorization,
                            AlgoliaGetSecuredSearchApiKeyScopes.STORE_OWNER_ID_NESTED
                        )
                    ) {
                        is Result.Error -> { }

                        is Result.Success -> {
                            var query = _uiState.value.searchQuery
                            var filters: String? = null
                            when (_uiState.value.searchQuery) {
                                OrderSearchChips.ORDER_RATE_STATUS_FALSE.tag -> {
                                    query = ""
                                    filters = "rated:\"${false}\""
                                }
                                OrderSearchChips.ORDER_RATE_STATUS_TRUE.tag -> {
                                    query = ""
                                    filters = "rated:\"${true}\""
                                }
                            }
                            _orderHits.value = searchRepository.pagingWithScopedApiKey(
                                query = query,
                                indexName = ALGOLIA_SEARCH_ORDERS_INDEX,
                                filters = filters,
                                scopedSecuredApiKey = rScopedKey.data
                            )
                        }
                    }
                }
            }
        }
    }
}