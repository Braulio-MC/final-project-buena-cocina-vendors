package com.bmc.buenacocinavendors.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.data.network.dto.CreateStoreDto
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateEmail
import com.bmc.buenacocinavendors.domain.usecase.ValidateImage
import com.bmc.buenacocinavendors.domain.usecase.ValidatePhoneNumber
import com.bmc.buenacocinavendors.domain.usecase.ValidateStoreDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateStoreName
import com.bmc.buenacocinavendors.ui.screen.store.StoreRegistrationFormIntent
import com.bmc.buenacocinavendors.ui.screen.store.StoreRegistrationFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreRegistrationViewModel @Inject constructor(
    private val auth0Account: Auth0,
    private val auth0Manager: SecureCredentialsManager,
    private val validateStoreName: ValidateStoreName,
    private val validateStoreDescription: ValidateStoreDescription,
    private val validateEmail: ValidateEmail,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val validateImage: ValidateImage,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StoreRegistrationFormUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: StoreRegistrationFormIntent) {
        when (intent) {
            is StoreRegistrationFormIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is StoreRegistrationFormIntent.EmailChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(email = intent.email)
                }
            }

            is StoreRegistrationFormIntent.ImageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(image = intent.image)
                }
            }

            is StoreRegistrationFormIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is StoreRegistrationFormIntent.PhoneChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(phoneNumber = intent.phone)
                }
            }

            is StoreRegistrationFormIntent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        val nameResult = validateStoreName(_uiState.value.name)
        val descriptionResult = validateStoreDescription(_uiState.value.description)
        val emailResult = validateEmail(_uiState.value.email)
        val phoneNumberResult = validatePhoneNumber(_uiState.value.phoneNumber)
        val imageResult = validateImage(_uiState.value.image)

        val hasErrors = listOf(
            nameResult,
            descriptionResult,
            emailResult,
            phoneNumberResult,
            imageResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                descriptionError = ( descriptionResult as? Result.Error)?.asFormErrorUiText(),
                emailError = (emailResult as? Result.Error)?.asFormErrorUiText(),
                phoneNumberError = (phoneNumberResult as? Result.Error)?.asFormErrorUiText(),
                imageError = (imageResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasErrors) {
            return
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isWaitingForResult = true)
            }
            when (val result = userRepository.getUserId()) {
                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(isWaitingForResult = false)
                    }
                }

                is Result.Success -> {
                    val dto = makeCreateStoreDto(result.data)
                    storeRepository.create(
                        dto,
                        onSuccess = {
                            processSuccess()
                        },
                        onFailure = { e ->
                            processFailure(e)
                        }
                    )
                }
            }
        }
    }

    private fun makeCreateStoreDto(userId: String): CreateStoreDto {
        return CreateStoreDto(
            name = _uiState.value.name,
            description = _uiState.value.description,
            email = _uiState.value.email,
            phoneNumber = _uiState.value.phoneNumber,
            image = _uiState.value.image!!,
            userId = userId
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

    fun startLogout(
        c: Context,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) {
        WebAuthProvider
            .logout(auth0Account)
            .withScheme(c.getString(R.string.com_auth0_scheme))
            .start(c, object :
                Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    onError()
                }

                override fun onSuccess(result: Void?) {
                    auth0Manager.clearCredentials()
                    onSuccess()
                }
            })
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}