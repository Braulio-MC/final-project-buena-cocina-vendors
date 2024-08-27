package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

data class DiscountTabDeleteUiState(
    val isWaitingForResult: Boolean = false,
    val discountDelete: DiscountDomain? = null,
    val discountDeleteError: UiText? = null,
)