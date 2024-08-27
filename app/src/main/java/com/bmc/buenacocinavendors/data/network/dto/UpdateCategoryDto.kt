package com.bmc.buenacocinavendors.data.network.dto

data class UpdateCategoryDto(
    val name: String,
    val parent: UpdateCategoryParentDto,
    val storeId: String
) {
    data class UpdateCategoryParentDto(
        val id: String,
        val name: String
    )
}