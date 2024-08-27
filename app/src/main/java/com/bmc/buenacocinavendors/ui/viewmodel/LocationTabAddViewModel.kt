package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.data.network.dto.CreateLocationDto
import com.bmc.buenacocinavendors.domain.repository.LocationRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocationDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocationName
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.add.LocationTabAddIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.add.LocationTabAddUiState
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

@HiltViewModel(assistedFactory = LocationTabAddViewModel.LocationTabAddViewModelFactory::class)
class LocationTabAddViewModel @AssistedInject constructor(
    private val validateLocationName: ValidateLocationName,
    private val validateLocationDescription: ValidateLocationDescription,
    private val locationRepository: LocationRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationTabAddUiState())
    val uiState = _uiState.asStateFlow()
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onIntent(intent: LocationTabAddIntent) {
        when (intent) {
            is LocationTabAddIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is LocationTabAddIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            LocationTabAddIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateLocationName(_uiState.value.name)
        val locationResult = validateLocationDescription(_uiState.value.description)

        val hasError = listOf(
            nameResult,
            locationResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                descriptionError = (locationResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val dto = makeCreateLocationDto()
        locationRepository.create(
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun makeCreateLocationDto(): CreateLocationDto {
        return CreateLocationDto(
            name = _uiState.value.name,
            description = _uiState.value.description,
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
    interface LocationTabAddViewModelFactory {
        fun create(storeId: String): LocationTabAddViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}