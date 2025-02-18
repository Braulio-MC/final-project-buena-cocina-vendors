package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.mapSalesDatesToDayOfWeek
import com.bmc.buenacocinavendors.domain.model.InsightCalculateSalesByDayOfWeekDomain
import com.bmc.buenacocinavendors.domain.model.StoreDomain
import com.bmc.buenacocinavendors.domain.repository.InsightRepository
import com.bmc.buenacocinavendors.ui.screen.home.HomeUiState
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
    private val insightRepository: InsightRepository,
    connectivityRepository: ConnectivityRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState
        .onStart {
            _store
                .onStart {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }
                .onEach { store ->
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, store = store.firstOrNull())
                    }
                }
                .launchIn(viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = HomeUiState()
        )
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unknown
        )
    private val _weeklyMappedSales =
        MutableStateFlow<List<Pair<String, InsightCalculateSalesByDayOfWeekDomain?>>>(emptyList())
    val weeklyMappedSales = _weeklyMappedSales

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _store: Flow<List<StoreDomain>> = flow {
        emit(userRepository.getUserId())
    }.flatMapLatest { result ->
        when (result) {
            is Result.Error -> {
                flowOf(emptyList())
            }

            is Result.Success -> {
                val q: (Query) -> Query = { query ->
                    query.whereEqualTo("userId", result.data)
                    query.limit(1)
                }
                storeRepository.get(q)
            }
        }
    }

    fun calculateSalesByDayOfWeek() {
        uiState.value.store?.id?.let { storeId ->
            _uiState.update { currentState ->
                currentState.copy(isLoadingWeeklySales = true)
            }
            viewModelScope.launch {
                when (val response = insightRepository.calculateSalesByDayOfWeek(storeId)) {
                    is Result.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(isLoadingWeeklySales = false)
                        }
                    }

                    is Result.Success -> {
                        val now = LocalDate.now()
                        val sevenDaysAgo = now.minusDays(7)
                        _uiState.update { currentState ->
                            currentState.copy(isLoadingWeeklySales = false)
                        }
                        _weeklyMappedSales.update {
                            response.data.mapSalesDatesToDayOfWeek(sevenDaysAgo, now)
                        }
                    }
                }
            }
        }
    }
}