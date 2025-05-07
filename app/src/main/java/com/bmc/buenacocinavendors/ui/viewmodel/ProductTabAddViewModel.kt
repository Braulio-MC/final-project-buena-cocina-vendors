package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.dto.CreateProductDto
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscount
import com.bmc.buenacocinavendors.domain.usecase.ValidateImage
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductName
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductPrice
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductQuantity
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add.ProductTabAddIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add.ProductTabAddUiState
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

@HiltViewModel(assistedFactory = ProductTabAddViewModel.ProductTabAddViewModelFactory::class)
class ProductTabAddViewModel @AssistedInject constructor(
    private val validateName: ValidateProductName,
    private val validateDescription: ValidateProductDescription,
    private val validatePrice: ValidateProductPrice,
    private val validateImage: ValidateImage,
    private val validateQuantity: ValidateProductQuantity,
    private val validateDiscount: ValidateDiscount,
    private val productRepository: ProductRepository,
    @Assisted("storeId") private val storeId: String,
    @Assisted("storeName") private val storeName: String,
    @Assisted("storeOwnerId") private val storeOwnerId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductTabAddUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: ProductTabAddIntent) {
        when (intent) {
            is ProductTabAddIntent.AddCategory -> {
                _uiState.update { currentState ->
                    val categories = currentState.categories.toMutableList()
                    if (!categories.contains(intent.category)) {
                        categories.add(intent.category)
                    }
                    currentState.copy(categories = categories)
                }
            }

            is ProductTabAddIntent.RemoveCategory -> {
                _uiState.update { currentState ->
                    val categories = currentState.categories.toMutableList()
                    categories.remove(intent.category)
                    currentState.copy(categories = categories)
                }
            }

            is ProductTabAddIntent.DefaultDiscountChanged -> {
                defaultDiscountChanged(intent)
            }

            is ProductTabAddIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is ProductTabAddIntent.DiscountChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        discount = intent.discount,
                        useDefaultDiscount = false
                    )
                }
            }

            is ProductTabAddIntent.ImageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(image = intent.image)
                }
            }

            is ProductTabAddIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is ProductTabAddIntent.PriceChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(price = intent.price)
                }
            }

            is ProductTabAddIntent.QuantityChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(quantity = intent.quantity)
                }
            }

            ProductTabAddIntent.Submit -> {
                submit()
            }
        }
    }

    private fun defaultDiscountChanged(intent: ProductTabAddIntent.DefaultDiscountChanged) {
        viewModelScope.launch {
            if (intent.use && intent.default == null) {
                val e = Exception("El descuento por defecto no se pudo obtener") // Custom exception here
                _validationEvent.send(ValidationEvent.Failure(e))
                return@launch
            }
            _uiState.update { currentState ->
                currentState.copy(
                    useDefaultDiscount = intent.use,
                    discount = if (!intent.use) null else intent.default
                )
            }
        }
    }

    private fun submit() {
        val nameResult = validateName(_uiState.value.name)
        val descriptionResult = validateDescription(_uiState.value.description)
        val priceResult = validatePrice(_uiState.value.price)
        val imageResult = validateImage(_uiState.value.image)
        val quantityResult = validateQuantity(_uiState.value.quantity)
        val discountResult = validateDiscount(_uiState.value.discount)

        val hasErrors = listOf(
            nameResult,
            descriptionResult,
            priceResult,
            imageResult,
            quantityResult,
            discountResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                nameError = (nameResult as? Result.Error)?.asFormErrorUiText(),
                descriptionError = (descriptionResult as? Result.Error)?.asFormErrorUiText(),
                priceError = (priceResult as? Result.Error)?.asFormErrorUiText(),
                imageError = (imageResult as? Result.Error)?.asFormErrorUiText(),
                quantityError = (quantityResult as? Result.Error)?.asFormErrorUiText(),
                discountError = (discountResult as? Result.Error)?.asFormErrorUiText()
            )
        }

        if (hasErrors) {
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isWaitingForResult = true)
        }
        val dto = makeCreateProductDto()
        productRepository.create(
            dto,
            onSuccess = {
                processSuccess()
            },
            onFailure = { message, details ->
                processFailure(Exception(message))
            }
        )
    }

    private fun makeCreateProductDto(): CreateProductDto {
        return CreateProductDto(
            name = _uiState.value.name,
            description = _uiState.value.description,
            price = _uiState.value.price.toDouble(),
            image = _uiState.value.image!!,
            quantity = _uiState.value.quantity.toInt(),
            categories = _uiState.value.categories.map { category ->
                CreateProductDto.CreateProductCategoryDto(
                    id = category.id,
                    name = category.name
                )
            },
            store = CreateProductDto.CreateProductStoreDto(
                id = storeId,
                name = storeName,
                ownerId = storeOwnerId
            ),
            discount = CreateProductDto.CreateProductDiscountDto(
                id = _uiState.value.discount!!.id,
                percentage = _uiState.value.discount!!.percentage.toDouble(),
                startDate = DateUtils.localDateTimeToFirebaseTimestamp(_uiState.value.discount!!.startDate!!),
                endDate = DateUtils.localDateTimeToFirebaseTimestamp(_uiState.value.discount!!.endDate!!)
            ),
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
    interface ProductTabAddViewModelFactory {
        fun create(
            @Assisted("storeId") storeId: String,
            @Assisted("storeName") storeName: String,
            @Assisted("storeOwnerId") storeOwnerId: String
        ): ProductTabAddViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}