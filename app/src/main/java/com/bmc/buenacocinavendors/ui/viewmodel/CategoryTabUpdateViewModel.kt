package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.data.network.dto.UpdateCategoryDto
import com.bmc.buenacocinavendors.domain.repository.CategoryRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateCategory
import com.bmc.buenacocinavendors.domain.usecase.ValidateCategoryName
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update.CategoryTabUpdateIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update.CategoryTabUpdateUiState
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

@HiltViewModel(assistedFactory = CategoryTabUpdateViewModel.CategoryTabUpdateViewModelFactory::class)
class CategoryTabUpdateViewModel @AssistedInject constructor(
    private val validateCategoryName: ValidateCategoryName,
    private val validateCategory: ValidateCategory,
    private val categoryRepository: CategoryRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryTabUpdateUiState())
    val uiState = _uiState.asStateFlow()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onIntent(intent: CategoryTabUpdateIntent) {
        when (intent) {
            is CategoryTabUpdateIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is CategoryTabUpdateIntent.ParentChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(currentParent = intent.parent)
                }
            }

            is CategoryTabUpdateIntent.CategoryUpdateChanged -> {
                val categoryName = intent.category?.name ?: ""
                _uiState.update { currentState ->
                    currentState.copy(
                        name = categoryName,
                        currentCategoryUpdate = intent.category
                    )
                }
            }

            CategoryTabUpdateIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateCategoryName(_uiState.value.name)
        val categoryResult = validateCategory(_uiState.value.currentCategoryUpdate)

        val hasError = listOf(
            nameResult,
            categoryResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                currentCategoryUpdateError = (categoryResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val dto = makeUpdateCategoryDto()
        val currentCategoryUpdateId = _uiState.value.currentCategoryUpdate!!.id
        categoryRepository.update(
            currentCategoryUpdateId,
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun makeUpdateCategoryDto(): UpdateCategoryDto {
        var parentId = ""
        var parentName = ""
        _uiState.value.currentParent?.let { parent ->
            parentId = parent.id
            parentName = parent.name
        }
        return UpdateCategoryDto(
            name = _uiState.value.name,
            parent = UpdateCategoryDto.UpdateCategoryParentDto(
                id = parentId,
                name = parentName
            ),
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
    interface CategoryTabUpdateViewModelFactory {
        fun create(storeId: String): CategoryTabUpdateViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}