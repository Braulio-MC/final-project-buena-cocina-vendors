package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update

import android.net.Uri
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.domain.model.ProductDomain

sealed class ProductTabUpdateIntent {
    data class ProductUpdateChanged(val product: ProductDomain? = null) : ProductTabUpdateIntent()
    data class NameChanged(val name: String) : ProductTabUpdateIntent()
    data class DescriptionChanged(val description: String) : ProductTabUpdateIntent()
    data class PriceChanged(val price: String) : ProductTabUpdateIntent()
    data class ImageChanged(val image: Uri?) : ProductTabUpdateIntent()
    data class QuantityChanged(val quantity: String) : ProductTabUpdateIntent()
    data class AddCategory(val category: ProductDomain.ProductCategoryDomain) : ProductTabUpdateIntent()
    data class RemoveCategory(val category: ProductDomain.ProductCategoryDomain) : ProductTabUpdateIntent()
    data class DiscountChanged(val discount: DiscountDomain? = null) : ProductTabUpdateIntent()
    data class DefaultDiscountChanged(val use: Boolean, val default: DiscountDomain?) :
        ProductTabUpdateIntent()

    data object Submit : ProductTabUpdateIntent()
}