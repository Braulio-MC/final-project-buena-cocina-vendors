package com.bmc.buenacocinavendors.data.network.dto

import android.net.Uri
import com.google.firebase.Timestamp

data class UpdateProductDto(
    val name: String,
    val description: String,
    val price: Double,
    val image: Uri?,
    val oldPath: String?,
    val quantity: Int,
    val category: UpdateProductCategoryDto,
    val store: UpdateProductStoreDto,
    val discount: UpdateProductDiscountDto
) {
    data class UpdateProductCategoryDto(
        val id: String,
        val name: String,
        val parentName: String
    )

    data class UpdateProductStoreDto(
        val id: String,
        val name: String
    )

    data class UpdateProductDiscountDto(
        val useDefault: Boolean,
        val id: String,
        val percentage: Double,
        val startDate: Timestamp,
        val endDate: Timestamp
    )
}