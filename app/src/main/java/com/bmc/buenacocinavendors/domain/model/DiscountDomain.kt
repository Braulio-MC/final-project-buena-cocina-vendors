package com.bmc.buenacocinavendors.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class DiscountDomain(
    val id: String,
    val name: String,
    val percentage: BigDecimal,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val storeId: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
