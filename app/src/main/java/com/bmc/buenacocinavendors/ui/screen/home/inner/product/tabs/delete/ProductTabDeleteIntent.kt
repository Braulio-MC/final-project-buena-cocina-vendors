package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete

import com.bmc.buenacocinavendors.domain.model.ProductDomain

sealed class ProductTabDeleteIntent {
    data class ProductDeleteChanged(val product: ProductDomain? = null) : ProductTabDeleteIntent()
    data object Submit : ProductTabDeleteIntent()
}