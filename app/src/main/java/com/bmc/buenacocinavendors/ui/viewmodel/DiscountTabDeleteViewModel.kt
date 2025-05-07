package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.domain.repository.DiscountRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscount
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete.DiscountTabDeleteIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete.DiscountTabDeleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountTabDeleteViewModel @Inject constructor(
    private val validateDiscount: ValidateDiscount,
    private val discountRepository: DiscountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscountTabDeleteUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: DiscountTabDeleteIntent) {
        when (intent) {
            is DiscountTabDeleteIntent.DiscountDeleteChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(discountDelete = intent.discount)
                }
            }

            DiscountTabDeleteIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val discountResult = validateDiscount(_uiState.value.discountDelete)

        val hasError = listOf(
            discountResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(discountDeleteError = (discountResult as? Result.Error)?.asFormErrorUiText())
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val discountId = _uiState.value.discountDelete!!.id
        discountRepository.delete(
            discountId,
            onSuccess = { message, affectedProducts ->
                processSuccess(message, affectedProducts)
            },
            onFailure = { message, details ->
                processFailure(message, details)
            }
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

    sealed class ValidationEvent {
        data class Success(val message: String, val affectedProducts: Int) : ValidationEvent()
        data class Failure(val message: String, val details: String) : ValidationEvent()
    }
}