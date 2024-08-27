package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.data.network.dto.UpdateLocationDto
import com.bmc.buenacocinavendors.domain.repository.LocationRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocation
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocationDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocationName
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update.LocationTabUpdateIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update.LocationTabUpdateUiState
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

@HiltViewModel(assistedFactory = LocationTabUpdateViewModel.LocationTabUpdateViewModelFactory::class)
class LocationTabUpdateViewModel @AssistedInject constructor(
    private val validateLocationName: ValidateLocationName,
    private val validateLocationDescription: ValidateLocationDescription,
    private val validateLocation: ValidateLocation,
    private val locationRepository: LocationRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationTabUpdateUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvents = Channel<ValidationEvent>()
    val validationEvents = _validationEvents.receiveAsFlow()

    fun onIntent(intent: LocationTabUpdateIntent) {
        when (intent) {
            is LocationTabUpdateIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is LocationTabUpdateIntent.LocationUpdateChanged -> {
                val locationName = intent.location?.name ?: ""
                val locationDesc = intent.location?.description ?: ""
                _uiState.update { currentState ->
                    currentState.copy(
                        name = locationName,
                        description = locationDesc,
                        currentLocationUpdate = intent.location
                    )
                }
            }

            is LocationTabUpdateIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            LocationTabUpdateIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateLocationName(_uiState.value.name)
        val descriptionResult = validateLocationDescription(_uiState.value.description)
        val locationResult = validateLocation(_uiState.value.currentLocationUpdate)

        val hasError = listOf(
            nameResult,
            descriptionResult,
            locationResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                descriptionError = (descriptionResult as? Result.Error)?.asFormErrorUiText(),
                currentLocationUpdateError = (locationResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val locationId = _uiState.value.currentLocationUpdate!!.id
        val dto = makeUpdateLocationDto()
        locationRepository.update(
            locationId,
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { e ->
                processFailure(e)
            }
        )
    }

    private fun makeUpdateLocationDto(): UpdateLocationDto {
        return UpdateLocationDto(
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
            _validationEvents.send(ValidationEvent.Success)
        }
    }

    private fun processFailure(e: Exception) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = false)
            }
            _validationEvents.send(ValidationEvent.Failure(e))
        }
    }

    @AssistedFactory
    interface LocationTabUpdateViewModelFactory {
        fun create(storeId: String): LocationTabUpdateViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}