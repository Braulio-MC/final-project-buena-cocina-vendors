package com.bmc.buenacocinavendors.domain.model

import java.time.LocalDateTime

data class StoreReviewDomain(
    val id: String,
    val userId: String,
    val storeId: String,
    val rating: Float,
    val comment: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
