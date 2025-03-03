package com.bmc.buenacocinavendors.data.network.dto

import com.google.firebase.Timestamp

data class CreateDiscountDto(
    val name: String,
    val percentage: Double,
    val startDate: Timestamp,
    val endDate: Timestamp,
    val storeId: String
)
