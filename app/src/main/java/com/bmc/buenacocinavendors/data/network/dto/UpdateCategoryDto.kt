package com.bmc.buenacocinavendors.data.network.dto

data class UpdateCategoryDto(
    val currentName: String,
    val name: String,
    val storeId: String
)