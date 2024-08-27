package com.bmc.buenacocinavendors.data.network.dto

data class CreateLocationDto(
    val name: String,
    val description: String,
    val storeId: String
)