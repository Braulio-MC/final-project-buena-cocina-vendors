package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.core.TimeUtils
import com.bmc.buenacocinavendors.data.network.dto.CreateDiscountDto
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountDateConsistency
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountName
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountPercentage
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add.DiscountTabAddIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add.DiscountTabAddUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@HiltViewModel(assistedFactory = DiscountTabAddViewModel.DiscountTabAddViewModelFactory::class)
class DiscountTabAddViewModel @AssistedInject constructor(
    private val validateName: ValidateDiscountName,
    private val validatePercentage: ValidateDiscountPercentage,
    private val validateDateConsistency: ValidateDiscountDateConsistency,
    private val discountRepository: DiscountRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscountTabAddUiState())
    val uiState = _uiState.asStateFlow()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvent = validationEventChannel.receiveAsFlow()

    fun onIntent(intent: DiscountTabAddIntent) {
        when (intent) {
            is DiscountTabAddIntent.EndDateChanged -> {
                val date = DateUtils.convertMillisToLocalDateTime(intent.endDate)
                _uiState.update { currentState ->
                    currentState.copy(endDate = date)
                }
            }

            is DiscountTabAddIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is DiscountTabAddIntent.PercentageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(percentage = intent.percentage)
                }
            }

            is DiscountTabAddIntent.StartDateChanged -> {
                val date = DateUtils.convertMillisToLocalDateTime(intent.startDate)
                _uiState.update { currentState ->
                    currentState.copy(startDate = date)
                }
            }

            is DiscountTabAddIntent.EndTimeChanged -> {
                val time = TimeUtils.rawValuesToLocalTime(intent.hour, intent.minute)
                _uiState.update { currentState ->
                    currentState.copy(endTime = time)
                }
            }

            is DiscountTabAddIntent.StartTimeChanged -> {
                val time = TimeUtils.rawValuesToLocalTime(intent.hour, intent.minute)
                _uiState.update { currentState ->
                    currentState.copy(startTime = time)
                }
            }

            DiscountTabAddIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateName(_uiState.value.name)
        val percentageResult = validatePercentage(_uiState.value.percentage)
        val dateConsistencyResult = validateDateConsistency(
            _uiState.value.startDate,
            _uiState.value.startTime,
            _uiState.value.endDate,
            _uiState.value.endTime
        )

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                percentageError = (percentageResult as? Result.Error)?.asFormErrorUiText(),
                dateConsistencyError = (dateConsistencyResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        val hasError = listOf(
            nameResult,
            percentageResult,
            dateConsistencyResult,
        ).any { it is Result.Error }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val dateValues = (dateConsistencyResult as Result.Success).data
        val dto = makeCreateDiscountDto(dateValues)
        discountRepository.create(
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun makeCreateDiscountDto(list: List<LocalDateTime>): CreateDiscountDto {
        val start = DateUtils.localDateTimeToFirebaseTimestamp(list[0]) // 0 is start date
        val end = DateUtils.localDateTimeToFirebaseTimestamp(list[1]) // 1 is end date
        return CreateDiscountDto(
            name = _uiState.value.name,
            percentage = _uiState.value.percentage.toDouble(),
            startDate = start,
            endDate = end,
            storeId = storeId
        )
    }

    private fun processSuccess() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun processFailure(e: Exception) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            validationEventChannel.send(ValidationEvent.Failure(e))
        }
    }

    @AssistedFactory
    interface DiscountTabAddViewModelFactory {
        fun create(storeId: String): DiscountTabAddViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}