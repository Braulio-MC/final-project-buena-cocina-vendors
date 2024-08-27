package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete

import com.bmc.buenacocinavendors.domain.model.DiscountDomain

sealed class DiscountTabDeleteIntent {
    data class DiscountDeleteChanged(val discount: DiscountDomain? = null) : DiscountTabDeleteIntent()
    data object Submit : DiscountTabDeleteIntent()
}