package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add

import android.net.Uri
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

sealed class ProductTabAddIntent {
    data class NameChanged(val name: String) : ProductTabAddIntent()
    data class DescriptionChanged(val description: String) : ProductTabAddIntent()
    data class PriceChanged(val price: String) : ProductTabAddIntent()
    data class ImageChanged(val image: Uri?) : ProductTabAddIntent()
    data class QuantityChanged(val quantity: String) : ProductTabAddIntent()
    data class CategoryChanged(val category: CategoryDomain? = null) : ProductTabAddIntent()
    data class DiscountChanged(val discount: DiscountDomain? = null) : ProductTabAddIntent()
    data class DefaultDiscountChanged(val use: Boolean, val default: DiscountDomain?) :
        ProductTabAddIntent()

    data object Submit : ProductTabAddIntent()
}