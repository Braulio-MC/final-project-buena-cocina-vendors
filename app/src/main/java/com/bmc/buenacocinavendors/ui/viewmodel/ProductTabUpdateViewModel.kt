package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.dto.UpdateProductDto
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asFormErrorUiText
import com.bmc.buenacocinavendors.domain.repository.ProductRepository
import com.bmc.buenacocinavendors.domain.usecase.ValidateDiscount
import com.bmc.buenacocinavendors.domain.usecase.ValidateImage
import com.bmc.buenacocinavendors.domain.usecase.ValidateProduct
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductDescription
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductName
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductPrice
import com.bmc.buenacocinavendors.domain.usecase.ValidateProductQuantity
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update.ProductTabUpdateIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update.ProductTabUpdateUiState
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

@HiltViewModel(assistedFactory = ProductTabUpdateViewModel.ProductTabUpdateViewModelFactory::class)
class ProductTabUpdateViewModel @AssistedInject constructor(
    private val validateProduct: ValidateProduct,
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
    private val _uiState = MutableStateFlow(ProductTabUpdateUiState())
    val uiState = _uiState.asStateFlow()
    private val _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntent(intent: ProductTabUpdateIntent) {
        when (intent) {
            is ProductTabUpdateIntent.AddCategory -> {
                _uiState.update { currentState ->
                    val categories = currentState.categories.toMutableList()
                    if (!categories.contains(intent.category)) {
                        categories.add(intent.category)
                    }
                    currentState.copy(categories = categories)
                }
            }

            is ProductTabUpdateIntent.RemoveCategory -> {
                _uiState.update { currentState ->
                    val categories = currentState.categories.toMutableList()
                    categories.remove(intent.category)
                    currentState.copy(categories = categories)
                }
            }

            is ProductTabUpdateIntent.DefaultDiscountChanged -> {
                defaultDiscountChanged(intent)
            }

            is ProductTabUpdateIntent.DescriptionChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is ProductTabUpdateIntent.DiscountChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        discount = intent.discount,
                        useDefaultDiscount = false
                    )
                }
            }

            is ProductTabUpdateIntent.ImageChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(image = intent.image)
                }
            }

            is ProductTabUpdateIntent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is ProductTabUpdateIntent.PriceChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(price = intent.price)
                }
            }

            is ProductTabUpdateIntent.ProductUpdateChanged -> {
                val name = intent.product?.name ?: ""
                val description = intent.product?.description ?: ""
                val price = intent.product?.price.toString()
                val image = intent.product?.image?.toUri()
                val quantity = intent.product?.quantity.toString()
                val categories = intent.product?.categories ?: emptyList()
                _uiState.update { currentState ->
                    currentState.copy(
                        productUpdate = intent.product,
                        name = name,
                        description = description,
                        price = price,
                        image = image,
                        quantity = quantity,
                        categories = categories
                    )
                }
            }

            is ProductTabUpdateIntent.QuantityChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(quantity = intent.quantity)
                }
            }

            ProductTabUpdateIntent.Submit -> {
                submit()
            }
        }
    }

    private fun defaultDiscountChanged(intent: ProductTabUpdateIntent.DefaultDiscountChanged) {
        viewModelScope.launch {
            if (intent.use && intent.default == null) {
                val e =
                    Exception("El descuento por defecto no se pudo obtener") // Custom exception here
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
        val productUpdateResult = validateProduct(_uiState.value.productUpdate)
        val nameResult = validateName(_uiState.value.name)
        val descriptionResult = validateDescription(_uiState.value.description)
        val priceResult = validatePrice(_uiState.value.price)
        val imageResult = validateImage(_uiState.value.image)
        val quantityResult = validateQuantity(_uiState.value.quantity)
        val discountResult = validateDiscount(_uiState.value.discount)

        val hasErrors = listOf(
            productUpdateResult,
            nameResult,
            descriptionResult,
            priceResult,
            imageResult,
            quantityResult,
            discountResult
        ).any { it is Result.Error }

        _uiState.update { currentState ->
            currentState.copy(
                productUpdateError = (productUpdateResult as? Result.Error)?.asFormErrorUiText(),
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
        if (_uiState.value.productUpdate?.image?.toUri() == _uiState.value.image) {
            _uiState.update { currentState ->
                currentState.copy(image = null)
            }
        }
        val dto = makeUpdateProductDto()
        val productId = _uiState.value.productUpdate!!.id
        productRepository.update(
            productId,
            dto,
            onSuccess = { message, affectedProductFavorites, affectedShoppingCartProducts ->
                processSuccess()
            },
            onFailure = { message, details ->
                processFailure(Exception(details))
            }
        )
    }

    private fun makeUpdateProductDto(): UpdateProductDto {
        return UpdateProductDto(
            name = _uiState.value.name,
            description = _uiState.value.description,
            price = _uiState.value.price.toDouble(),
            image = _uiState.value.image,
            oldPath = _uiState.value.productUpdate?.image,
            quantity = _uiState.value.quantity.toInt(),
            categories = _uiState.value.categories.map { category ->
                UpdateProductDto.UpdateProductCategoryDto(
                    id = category.id,
                    name = category.name
                )
            },
            store = UpdateProductDto.UpdateProductStoreDto(
                id = storeId,
                name = storeName,
                ownerId = storeOwnerId
            ),
            discount = UpdateProductDto.UpdateProductDiscountDto(
                useDefault = _uiState.value.useDefaultDiscount,
                id = _uiState.value.discount!!.id,
                percentage = _uiState.value.discount!!.percentage.toDouble(),
                startDate = DateUtils.localDateTimeToFirebaseTimestamp(_uiState.value.discount!!.startDate!!),
                endDate = DateUtils.localDateTimeToFirebaseTimestamp(_uiState.value.discount!!.endDate!!)
            )
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
    interface ProductTabUpdateViewModelFactory {
        fun create(
            @Assisted("storeId") storeId: String,
            @Assisted("storeName") storeName: String,
            @Assisted("storeOwnerId") storeOwnerId: String
        ): ProductTabUpdateViewModel
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Failure(val error: Exception) : ValidationEvent()
    }
}