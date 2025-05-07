package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.core.TimeUtils
import com.bmc.buenacocinavendors.data.network.dto.UpdateDiscountDto
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscount
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountDateConsistency
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountName
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscountPercentage
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.update.DiscountTabUpdateIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.update.DiscountTabUpdateUiState
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

@HiltViewModel(assistedFactory = DiscountTabUpdateViewModel.DiscountTabUpdateViewModelFactory::class)
class DiscountTabUpdateViewModel @AssistedInject constructor(
    private val validateDiscount: ValidateDiscount,
    private val validateName: ValidateDiscountName,
    private val validatePercentage: ValidateDiscountPercentage,
    private val validateDiscountDateConsistency: ValidateDiscountDateConsistency,
    private val discountRepository: DiscountRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscountTabUpdateUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: DiscountTabUpdateIntent) {
        when (intent) {
            is DiscountTabUpdateIntent.EndDateChanged -> {
                val date = DateUtils.convertMillisToLocalDateTime(intent.date)
                _uiState.update { currentState ->
                    currentState.copy(endDate = date)
                }

            }

            is DiscountTabUpdateIntent.EndTimeChanged -> {
                val time = TimeUtils.rawValuesToLocalTime(intent.hour, intent.minute)
                _uiState.update { currentState ->
                    currentState.copy(endTime = time)
                }
            }

            is DiscountTabUpdateIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is DiscountTabUpdateIntent.PercentageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(percentage = intent.percentage)
                }
            }

            is DiscountTabUpdateIntent.StartDateChanged -> {
                val date = DateUtils.convertMillisToLocalDateTime(intent.date)
                _uiState.update { currentState ->
                    currentState.copy(startDate = date)
                }
            }

            is DiscountTabUpdateIntent.StartTimeChanged -> {
                val time = TimeUtils.rawValuesToLocalTime(intent.hour, intent.minute)
                _uiState.update { currentState ->
                    currentState.copy(startTime = time)
                }
            }

            is DiscountTabUpdateIntent.DiscountUpdateChanged -> {
                val discountName = intent.discount?.name ?: ""
                val discountPercentage = intent.discount?.percentage?.toString() ?: ""
                val discountStartDate = intent.discount?.startDate
                val discountStartTime = discountStartDate?.let {
                    TimeUtils.rawValuesToLocalTime(it.hour, it.minute)
                }
                val discountEndDate = intent.discount?.endDate
                val discountEndTime = discountEndDate?.let {
                    TimeUtils.rawValuesToLocalTime(it.hour, it.minute)
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        name = discountName,
                        percentage = discountPercentage,
                        startDate = discountStartDate,
                        startTime = discountStartTime,
                        endDate = discountEndDate,
                        endTime = discountEndTime,
                        discountUpdate = intent.discount
                    )
                }
            }

            DiscountTabUpdateIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val discountResult = validateDiscount(_uiState.value.discountUpdate)
        val nameResult = validateName(_uiState.value.name)
        val percentageResult = validatePercentage(_uiState.value.percentage)
        val dateConsistencyResult = validateDiscountDateConsistency(
            _uiState.value.startDate,
            _uiState.value.startTime,
            _uiState.value.endDate,
            _uiState.value.endTime
        )

        _uiState.update { currentState ->
            currentState.copy(
                discountUpdateError = (discountResult as? Result.Error)?.asFormErrorUiText(),
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                percentageError = (percentageResult as? Result.Error)?.asFormErrorUiText(),
                dateConsistencyError = (dateConsistencyResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        val hasError = listOf(
            discountResult,
            nameResult,
            percentageResult,
            dateConsistencyResult
        ).any { it is Result.Error }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val discountId = _uiState.value.discountUpdate!!.id
        val dateValues = (dateConsistencyResult as Result.Success).data
        val dto = makeUpdateDiscountDto(dateValues)
        discountRepository.update(
            discountId,
            dto,
            onSuccess = { message, affectedProducts ->
                processSuccess(message, affectedProducts)
            },
            onFailure = { message, details ->
                processFailure(message, details)
            }
        )
    }

    private fun makeUpdateDiscountDto(list: List<LocalDateTime>): UpdateDiscountDto {
        val start = DateUtils.localDateTimeToFirebaseTimestamp(list[0]) // 0 is start date
        val end = DateUtils.localDateTimeToFirebaseTimestamp(list[1]) // 1 is end date
        return UpdateDiscountDto(
            name = _uiState.value.name,
            percentage = _uiState.value.percentage.toDouble(),
            startDate = start,
            endDate = end,
            storeId = storeId
        )
    }

    private fun processSuccess(message: String, affectedProducts: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidationEvent.Success(message, affectedProducts))
        }
    }

    private fun processFailure(message: String, details: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidationEvent.Failure(message, details))
        }
    }

    @AssistedFactory
    interface DiscountTabUpdateViewModelFactory {
        fun create(storeId: String): DiscountTabUpdateViewModel
    }

    sealed interface ValidationEvent {
        data class Success(val message: String, val affectedProducts: Int) : ValidationEvent
        data class Failure(val message: String, val details: String) : ValidationEvent
    }
}