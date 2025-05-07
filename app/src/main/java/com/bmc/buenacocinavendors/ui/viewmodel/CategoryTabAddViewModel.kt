package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.data.network.dto.CreateCategoryDto
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateCategoryName
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add.CategoryTabAddIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add.CategoryTabAddUiState
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

@HiltViewModel(assistedFactory = CategoryTabAddViewModel.CategoryTabAddViewModelFactory::class)
class CategoryTabAddViewModel @AssistedInject constructor(
    private val validateCategoryName: ValidateCategoryName,
    private val categoryRepository: CategoryRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryTabAddUiState())
    val uiState = _uiState.asStateFlow()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onIntent(intent: CategoryTabAddIntent) {
        when (intent) {
            is CategoryTabAddIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            CategoryTabAddIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateCategoryName(uiState.value.name)

        val hasError = listOf(
            nameResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val dto = makeCreateCategoryDto()
        categoryRepository.create(
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun makeCreateCategoryDto(): CreateCategoryDto {
        return CreateCategoryDto(
            name = _uiState.value.name,
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
    interface CategoryTabAddViewModelFactory {
        fun create(storeId: String): CategoryTabAddViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}