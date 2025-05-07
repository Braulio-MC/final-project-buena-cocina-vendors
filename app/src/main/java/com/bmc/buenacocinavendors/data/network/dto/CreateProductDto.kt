package com.bmc.buenacocinavendors.data.network.dto

import android.net.Uri
import com.google.firebase.Timestamp

data class CreateProductDto(
    val name: String,
    val description: String,
    val price: Double,
    val image: Uri,
    val quantity: Int,
    val categories: List<CreateProductCategoryDto>,
    val store: CreateProductStoreDto,
    val discount: CreateProductDiscountDto
) {
    data class CreateProductCategoryDto(
        val id: String,
        val name: String
    )

    data class CreateProductStoreDto(
        val id: String,
        val name: String,
        val ownerId: String
    )

    data class CreateProductDiscountDto(
        val id: String,
        val percentage: Double,
        val startDate: Timestamp,
        val endDate: Timestamp
    )
}