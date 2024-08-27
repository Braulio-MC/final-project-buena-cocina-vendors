package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.ProductDomain

data class ProductTabDeleteUiState(
    val isWaitingForResult: Boolean = false,
    val productDelete: ProductDomain? = null,
    val productDeleteError: UiText? = null,
)