package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateCategory
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.delete.CategoryTabDeleteIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.delete.CategoryTabDeleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryTabDeleteViewModel @Inject constructor(
    private val validateCategory: ValidateCategory,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryTabDeleteUiState())
    val uiState = _uiState.asStateFlow()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onIntent(intent: CategoryTabDeleteIntent) {
        when (intent) {
            is CategoryTabDeleteIntent.CategoryDeleteChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(categoryDelete = intent.category)
                }
            }

            CategoryTabDeleteIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val categoryResult = validateCategory(_uiState.value.categoryDelete)

        val hasError = listOf(
            categoryResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                categoryDeleteError = (categoryResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val categoryId = _uiState.value.categoryDelete!!.id
        categoryRepository.delete(
            categoryId,
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

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}