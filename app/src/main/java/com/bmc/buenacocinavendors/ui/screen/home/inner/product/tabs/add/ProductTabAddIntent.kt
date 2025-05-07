package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add

import android.net.Uri
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.domain.model.ProductDomain

sealed class ProductTabAddIntent {
    data class NameChanged(val name: String) : ProductTabAddIntent()
    data class DescriptionChanged(val description: String) : ProductTabAddIntent()
    data class PriceChanged(val price: String) : ProductTabAddIntent()
    data class ImageChanged(val image: Uri?) : ProductTabAddIntent()
    data class QuantityChanged(val quantity: String) : ProductTabAddIntent()
    data class AddCategory(val category: ProductDomain.ProductCategoryDomain) : ProductTabAddIntent()
    data class RemoveCategory(val category: ProductDomain.ProductCategoryDomain) : ProductTabAddIntent()
    data class DiscountChanged(val discount: DiscountDomain? = null) : ProductTabAddIntent()
    data class DefaultDiscountChanged(val use: Boolean, val default: DiscountDomain?) :
        ProductTabAddIntent()

    data object Submit : ProductTabAddIntent()
}