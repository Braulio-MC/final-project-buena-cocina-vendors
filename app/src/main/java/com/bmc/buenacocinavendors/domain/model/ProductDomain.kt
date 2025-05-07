package com.bmc.buenacocinavendors.domain.model

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

data class ProductDomain(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: BigDecimal,
    val quantity: BigInteger,
    val discount: ProductDiscountDomain,
    val store: ProductStoreDomain,
    val categories: List<ProductCategoryDomain>,
    val rating: BigDecimal,
    val totalRating: BigDecimal,
    val totalReviews: BigInteger,
    val updatedAt: LocalDateTime?,
    val createdAt: LocalDateTime?
) {
    data class ProductDiscountDomain (
        val id: String,
        val percentage: BigDecimal,
        val startDate: LocalDateTime?,
        val endDate: LocalDateTime?
    )

    data class ProductStoreDomain (
        val id: String,
        val name: String,
        val ownerId: String
    )

    data class ProductCategoryDomain (
        val id: String,
        val name: String
    )
}