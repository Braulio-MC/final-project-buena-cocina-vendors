package com.bmc.buenacocinavendors.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.LOCATION_RETRIEVE_INTERVAL_IN_MILLIS
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.data.network.service.LocationService
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.DataError
import com.bmc.buenacocinavendors.domain.isGpsOrNetworkEnabledFlow
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.mapper.asLatLng
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.bmc.buenacocinavendors.domain.repository.InsightRepository
import com.bmc.buenacocinavendors.domain.repository.OrderLineRepository
import com.bmc.buenacocinavendors.domain.repository.RemoteConfigRepository
import com.bmc.buenacocinavendors.domain.usecase.CreateGetStreamChannel
import com.bmc.buenacocinavendors.domain.usecase.SendOrderStatusNotificationToSpecificUserDevices
import com.bmc.buenacocinavendors.domain.usecase.UpdateOrderStatus
import com.bmc.buenacocinavendors.domain.usecase.ValidateOrderStatus
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderIntent
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@HiltViewModel(assistedFactory = DetailedOrderViewModel.DetailedOrderViewModelFactory::class)
class DetailedOrderViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val validateStatus: ValidateOrderStatus,
    private val sendOrderStatusNotificationToSpecificUserDevices: SendOrderStatusNotificationToSpecificUserDevices,
    private val updateOrderStatus: UpdateOrderStatus,
    private val createGetStreamChannel: CreateGetStreamChannel,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val orderRepository: OrderRepository,
    private val orderLineRepository: OrderLineRepository,
    private val locationService: LocationService,
    private val insightRepository: InsightRepository,
    connectivityRepository: ConnectivityRepository,
    @Assisted private val orderId: String
) : ViewModel() {
    private var _locationJob: Job? = null
    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue: StateFlow<List<String>> = _visiblePermissionDialogQueue
    private val _uiState = MutableStateFlow(DetailedOrderUiState())
    val uiState: StateFlow<DetailedOrderUiState> = _uiState
        .onStart {
            orderRepository
                .get(orderId)
                .onStart {
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingOrder = true)
                    }
                }
                .onEach { order ->
                    _uiState.update { currentState ->
                        val status = OrderStatus.entries.find { it.status == order?.status }
                            ?: OrderStatus.UNASSIGNED
                        currentState.copy(isLoadingOrder = false, order = order, status = status)
                    }
                }
                .launchIn(viewModelScope)
            orderLineRepository
                .get(orderId)
                .onStart {
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingOrderLines = true)
                    }
                }
                .onEach { lines ->
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingOrderLines = false, lines = lines)
                    }
                    calculate(lines)
                }
                .launchIn(viewModelScope)
            remoteConfigRepository.cuceiCenterOnMap
                .onEach { pair ->
                    _uiState.update { currentState ->
                        currentState.copy(cuceiCenterOnMap = pair)
                    }
                }
                .launchIn(viewModelScope)
            remoteConfigRepository.cuceiAreaBoundsOnMap
                .onEach { pairList ->
                    _uiState.update { currentState ->
                        currentState.copy(cuceiAreaBoundsOnMap = pairList)
                    }
                }
                .launchIn(viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = DetailedOrderUiState()
        )
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unknown
        )
    private val _validationEvent = Channel<ValidateEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: DetailedOrderIntent) {
        when (intent) {
            is DetailedOrderIntent.ChangeStatus -> {
                _uiState.update { currentState ->
                    currentState.copy(status = intent.status)
                }
            }

            DetailedOrderIntent.CreateChannel -> {
                createChannel()
            }

            DetailedOrderIntent.Submit -> {
                submit()
            }
        }
    }

    private fun calculate(items: List<OrderLineDomain>) {
        _uiState.update { currentState ->
            currentState.copy(isCalculatingOrderTotal = true)
        }
        if (items.isNotEmpty()) {
            val total =
                items.sumOf { item ->
                    val discount =
                        (item.product.price * (item.product.discount.percentage / BigDecimal.valueOf(
                            100
                        ))) * item.quantity.toBigDecimal()
                    item.product.price.times(item.quantity.toBigDecimal()).minus(discount)
                }.setScale(2, RoundingMode.HALF_DOWN)
            _uiState.update { currentState ->
                currentState.copy(
                    isCalculatingOrderTotal = false,
                    orderTotal = total
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isCalculatingOrderTotal = false,
                    orderTotal = BigDecimal.ZERO
                )
            }
        }
    }

    private fun createChannel() {
        uiState.value.order?.let { order ->
            _uiState.update { currentState ->
                currentState.copy(isWaitingForChannelResult = true)
            }
            viewModelScope.launch {
                val result = createGetStreamChannel(
                    orderId = order.id,
                    storeOwnerId = order.store.ownerId,
                    storeName = order.store.name,
                    userId = order.user.id,
                    userName = order.user.name
                )
                when (result) {
                    is Result.Error -> {
                        processCreateChannelFailed(result.error)
                    }

                    is Result.Success -> {
                        processCreateChannelSuccess(result.data)
                    }
                }
            }
        }
    }

    private fun submit() {
        val statusResult = validateStatus(_uiState.value.status)

        val hasError = listOf(
            statusResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(statusError = (statusResult as? Result.Error)?.asFormErrorUiText())
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForStatusResult = true)
        }
        updateOrderStatus(
            orderId,
            _uiState.value.status.status,
            onSuccess = {
                uiState.value.order?.let { order ->
                    sendOrderStatusNotificationToSpecificUserDevices(
                        userId = order.user.id,
                        storeName = order.store.name,
                        orderStatus = _uiState.value.status.status,
                        onSuccess = {
                            processUpdateStatusSuccess()
                        },
                        onFailure = { e ->
                            processUpdateStatusFailure(e)
                        }
                    )
                }
            },
            onFailure = { e ->
                processUpdateStatusFailure(e)
            }
        )
    }

    private fun processCreateChannelSuccess(channelId: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForChannelResult = false)
            }
            _validationEvent.send(ValidateEvent.CreateChannelSuccess(channelId))
        }
    }

    private fun processCreateChannelFailed(e: DataError) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForChannelResult = false)
            }
            _validationEvent.send(ValidateEvent.CreateChannelFailure(e))
        }
    }

    private fun processUpdateStatusSuccess() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForStatusResult = false)
            }
            _validationEvent.send(ValidateEvent.UpdateStatusSuccess)
        }
    }

    private fun processUpdateStatusFailure(e: Exception) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForStatusResult = false)
            }
            _validationEvent.send(ValidateEvent.UpdateStatusFailure(e))
        }
    }

    fun dismissPermissionDialog() {
        if (_visiblePermissionDialogQueue.value.isNotEmpty()) {
            _visiblePermissionDialogQueue.update { currentState ->
                currentState.drop(1)
            }
        }
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !_visiblePermissionDialogQueue.value.contains(permission)) {
            _visiblePermissionDialogQueue.update { currentState ->
                currentState + permission
            }
        }
    }

    fun startLocationUpdates() {
        if (_locationJob == null || _locationJob?.isActive == false) {
            _locationJob = viewModelScope.launch {
                combine(
                    locationService.getLocationUpdates(LOCATION_RETRIEVE_INTERVAL_IN_MILLIS)
                        .distinctUntilChanged()
                        .filterNotNull()
                        .map { location -> location.asLatLng() }
                        .catch { e -> e.printStackTrace() },
                    context.isGpsOrNetworkEnabledFlow()
                        .distinctUntilChanged()
                ) { location, isLocationEnabled ->
                    if (isLocationEnabled) location else null
                }.onStart {
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingUserLocation = true)
                    }
                }.onEach { location ->
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingUserLocation = false, userLocation = location)
                    }
                }.launchIn(viewModelScope)
            }
        }
        getTopLocationsOnMap()
    }

    fun stopLocationUpdates() {
        _locationJob?.cancel()
        _locationJob = null
        _uiState.update { currentState ->
            currentState.copy(userLocation = null)
        }
    }

    private fun getTopLocationsOnMap() {
        _uiState.update { currentState ->
            currentState.copy(isLoadingTopLocationsOnMap = true)
        }
        viewModelScope.launch {
            when (val response = insightRepository.getTopLocationsOnMap()) {
                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(isLoadingTopLocationsOnMap = false)
                    }
                }

                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoadingTopLocationsOnMap = false,
                            topLocationsOnMap = response.data
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface DetailedOrderViewModelFactory {
        fun create(orderId: String): DetailedOrderViewModel
    }

    sealed interface ValidateEvent {
        data object UpdateStatusSuccess : ValidateEvent
        data class UpdateStatusFailure(val error: Exception) : ValidateEvent
        data class CreateChannelSuccess(val channelId: String) : ValidateEvent
        data class CreateChannelFailure(val error: DataError) : ValidateEvent
    }
}