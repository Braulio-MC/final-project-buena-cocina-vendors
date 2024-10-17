package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.DataError
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.repository.OrderLineRepository
import com.bmc.buenacocinavendors.domain.usecase.CreateGetStreamChannel
import com.bmc.buenacocinavendors.domain.usecase.SendOrderStatusNotificationToSpecificUserDevices
import com.bmc.buenacocinavendors.domain.usecase.UpdateOrderStatus
import com.bmc.buenacocinavendors.domain.usecase.ValidateOrderStatus
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderIntent
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderUiResultState
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailedOrderViewModel.DetailedOrderViewModelFactory::class)
class DetailedOrderViewModel @AssistedInject constructor(
    private val validateStatus: ValidateOrderStatus,
    private val sendOrderStatusNotificationToSpecificUserDevices: SendOrderStatusNotificationToSpecificUserDevices,
    private val updateOrderStatus: UpdateOrderStatus,
    private val createGetStreamChannel: CreateGetStreamChannel,
    orderRepository: OrderRepository,
    orderLineRepository: OrderLineRepository,
    connectivityRepository: ConnectivityRepository,
    @Assisted private val orderId: String
) : ViewModel() {
    private val _resultState = MutableStateFlow(DetailedOrderUiResultState())
    val resultState: StateFlow<DetailedOrderUiResultState> = _resultState.asStateFlow()
    private val _order = orderRepository.get(orderId)
    private val _orderLines = orderLineRepository.get(orderId)
    val uiState: StateFlow<DetailedOrderUiState> = combine(
        _order,
        _orderLines
    ) { order, orderLines ->
        val status =
            OrderStatus.entries.find { it.status == order?.status } ?: OrderStatus.UNASSIGNED
        _resultState.update { currentState ->
            currentState.copy(status = status)
        }
        DetailedOrderUiState(
            order = order,
            lines = orderLines
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
        initialValue = DetailedOrderUiState(isLoadingResources = true)
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
                _resultState.update { currentState ->
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

    private fun createChannel() {
        uiState.value.order?.let { order ->
            _resultState.update { currentState ->
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
        val statusResult = validateStatus(_resultState.value.status)

        val hasError = listOf(
            statusResult
        ).any { it is Result.Error }

        _resultState.update { currentState ->
            currentState.copy(statusError = (statusResult as? Result.Error)?.asFormErrorUiText())
        }

        if (hasError) {
            return
        }

        _resultState.update { currentState ->
            currentState.copy(isWaitingForStatusResult = true)
        }
        updateOrderStatus(
            orderId,
            _resultState.value.status.status,
            onSuccess = {
                uiState.value.order?.let { order ->
                    sendOrderStatusNotificationToSpecificUserDevices(
                        userId = order.user.id,
                        storeName = order.store.name,
                        orderStatus = _resultState.value.status.status,
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
            _resultState.update { currentState ->
                currentState.copy(isWaitingForChannelResult = false)
            }
            _validationEvent.send(ValidateEvent.CreateChannelSuccess(channelId))
        }
    }

    private fun processCreateChannelFailed(e: DataError) {
        viewModelScope.launch {
            _resultState.update { currentState ->
                currentState.copy(isWaitingForChannelResult = false)
            }
            _validationEvent.send(ValidateEvent.CreateChannelFailure(e))
        }
    }

    private fun processUpdateStatusSuccess() {
        viewModelScope.launch {
            _resultState.update { currentState ->
                currentState.copy(isWaitingForStatusResult = false)
            }
            _validationEvent.send(ValidateEvent.UpdateStatusSuccess)
        }
    }

    private fun processUpdateStatusFailure(e: Exception) {
        viewModelScope.launch {
            _resultState.update { currentState ->
                currentState.copy(isWaitingForStatusResult = false)
            }
            _validationEvent.send(ValidateEvent.UpdateStatusFailure(e))
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