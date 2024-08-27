package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.domain.usecase.ValidateProduct
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete.ProductTabDeleteIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete.ProductTabDeleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductTabDeleteViewModel @Inject constructor(
    private val validateProduct: ValidateProduct,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductTabDeleteUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: ProductTabDeleteIntent) {
        when (intent) {
            is ProductTabDeleteIntent.ProductDeleteChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(productDelete = intent.product)
                }
            }

            ProductTabDeleteIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val productResult = validateProduct(_uiState.value.productDelete)

        val hasError = listOf(
            productResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                productDeleteError = (productResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val productId = _uiState.value.productDelete!!.id
        productRepository.delete(
            productId,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun processSuccess() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidationEvent.Success)
        }
    }

    private fun processFailure(e: Exception) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvent.send(ValidationEvent.Failure(e))
        }
    }

    sealed interface ValidationEvent {
        data object Success : ValidationEvent
        data class Failure(val error: Exception) : ValidationEvent
    }
}