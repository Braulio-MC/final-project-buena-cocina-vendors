package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add

import android.net.Uri
import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

data class ProductTabAddUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val price: String = "",
    val priceError: UiText? = null,
    val image: Uri? = null,
    val imageError: UiText? = null,
    val quantity: String = "",
    val quantityError: UiText? = null,
    val category: CategoryDomain? = null,
    val categoryError: UiText? = null,
    val discount: DiscountDomain? = null,
    val discountError: UiText? = null,
    val useDefaultDiscount: Boolean = false
)