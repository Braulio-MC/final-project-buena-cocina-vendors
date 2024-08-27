package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.domain.repository.LocationRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateLocation
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete.LocationTabDeleteIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete.LocationTabDeleteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationTabDeleteViewModel @Inject constructor(
    private val validateLocation: ValidateLocation,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationTabDeleteUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvents = Channel<ValidationEvent>()
    val validationEvents = _validationEvents.receiveAsFlow()

    fun onIntent(intent: LocationTabDeleteIntent) {
        when (intent) {
            is LocationTabDeleteIntent.LocationDeleteChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(locationDelete = intent.location)
                }
            }

            LocationTabDeleteIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val locationResult = validateLocation(_uiState.value.locationDelete)

        val hasError = listOf(
            locationResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                locationDeleteError = (locationResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasError) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val locationId = _uiState.value.locationDelete!!.id
        locationRepository.delete(
            locationId,
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

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}