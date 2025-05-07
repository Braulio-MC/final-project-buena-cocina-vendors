package com.bmc.buenacocinavendors.domain.model

import java.time.LocalDateTime

data class CategoryDomain(
    val id: String,
    val name: String,
    val storeId: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
