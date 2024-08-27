package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.data.network.dto.UpdateStoreDto
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.usecase.ValidateEmail
import com.bmc.buenacocinavendors.domain.usecase.ValidateImage
import com.bmc.buenacocinavendors.domain.usecase.ValidatePhoneNumber
import com.bmc.buenacocinavendors.domain.usecase.ValidateStoreDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateStoreName
import com.bmc.buenacocinavendors.ui.screen.home.inner.store.update.StoreUpdateIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.store.update.StoreUpdateUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = StoreUpdateViewModel.StoreUpdateViewModelFactory::class)
class StoreUpdateViewModel @AssistedInject constructor(
    private val validateStoreName: ValidateStoreName,
    private val validateStoreDescription: ValidateStoreDescription,
    private val validateEmail: ValidateEmail,
    private val validateImage: ValidateImage,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
    connectivityRepository: ConnectivityRepository,
    @Assisted private val storeId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(StoreUpdateUiState())
    val uiState = _uiState.asStateFlow()
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unavailable
        )
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvents = _validationEvent.receiveAsFlow()

    init {
        collectStore()
    }

    fun onIntent(intent: StoreUpdateIntent) {
        when (intent) {
            is StoreUpdateIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is StoreUpdateIntent.EmailChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(email = intent.email)
                }
            }

            is StoreUpdateIntent.ImageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(image = intent.image)
                }
            }

            is StoreUpdateIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is StoreUpdateIntent.PhoneChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(phoneNumber = intent.phone)
                }
            }

            StoreUpdateIntent.Submit -> {
                submit()
            }
        }
    }

    private fun collectStore() {
        storeRepository.get(storeId)
            .onEach { store ->
                _uiState.update { currentState ->
                    currentState.copy(
                        store = store,
                        name = store?.name ?: "",
                        description = store?.description ?: "",
                        email = store?.email ?: "",
                        phoneNumber = store?.phoneNumber ?: "",
                        image = store?.image?.toUri()
                    )
                }
            }
            .launchIn(viewModelScope)
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
                descriptionError = (descriptionResult as? Result.Error)?.asFormErrorUiText(),
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
            if (_uiState.value.store?.image?.toUri() == _uiState.value.image) {
                _uiState.update { currentState ->
                    currentState.copy(image = null)
                }
            }
            when (val resultUser = userRepository.getUserId()) {
                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(isWaitingForResult = false)
                    }
                }

                is Result.Success -> {
                    val dto = makeUpdateStoreDto(resultUser.data)
                    storeRepository.update(
                        storeId,
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

    private fun makeUpdateStoreDto(userId: String): UpdateStoreDto {
        return UpdateStoreDto(
            name = _uiState.value.name,
            description = _uiState.value.description,
            email = _uiState.value.email,
            phoneNumber = _uiState.value.phoneNumber,
            userId = userId,
            image = _uiState.value.image,
            oldPath = _uiState.value.store?.image
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

    @AssistedFactory
    interface StoreUpdateViewModelFactory {
        fun create(storeId: String): StoreUpdateViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}