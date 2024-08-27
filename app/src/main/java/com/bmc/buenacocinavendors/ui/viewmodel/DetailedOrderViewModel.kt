package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.data.network.dto.CreateMessageDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateOrderDto
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.repository.MessagingRepository
import com.bmc.buenacocinavendors.domain.repository.OrderLineRepository
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
    private val orderRepository: OrderRepository,
    private val messagingRepository: MessagingRepository,
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
        val status = OrderStatus.entries.find { it.status == order?.status } ?: OrderStatus.UNASSIGNED
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
            initialValue = NetworkStatus.Unavailable
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

            DetailedOrderIntent.Submit -> {
                submit()
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
            currentState.copy(isWaitingForResult = true)
        }
        val dto = UpdateOrderDto(
            status = _resultState.value.status.status
        )
        try {
            val messageDto = makeCreateMessageDto()
            orderRepository.update(
                orderId,
                dto,
                onSuccess = {
                    messagingRepository.sendMessageToTopic(
                        orderId,
                        messageDto,
                        onSuccess = {
                            processSuccess()
                        },
                        onFailure = { e ->
                            processFailure(e)
                        }
                    )
                    processSuccess()
                },
                onFailure = { e ->
                    processFailure(e)
                }
            )
        } catch (e: Exception) {
            processFailure(e)
        }
    }

    private fun processSuccess() {
        viewModelScope.launch {
            _resultState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidateEvent.Success)
        }
    }

    private fun processFailure(e: Exception) {
        viewModelScope.launch {
            _resultState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidateEvent.Failure(e))
        }
    }

    private fun makeCreateMessageDto(): CreateMessageDto {
        if (uiState.value.order == null) {
            throw Exception("Order is null") // Custom exception here
        }
        return CreateMessageDto(
            notification = CreateMessageDto.CreateMessageNotificationDto(
                title = "Actualización de tu pedido en ${uiState.value.order!!.store.name}",
                body = "Tu pedido se encuentra en estado: ${_resultState.value.status.status}"
            ),
            data = hashMapOf()
        )
    }

    @AssistedFactory
    interface DetailedOrderViewModelFactory {
        fun create(orderId: String): DetailedOrderViewModel
    }

    sealed interface ValidateEvent {
        data object Success : ValidateEvent
        data class Failure(val error: Exception) : ValidateEvent
    }
}