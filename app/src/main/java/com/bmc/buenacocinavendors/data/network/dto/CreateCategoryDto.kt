package com.bmc.buenacocinavendors.data.network.dto

data class CreateCategoryDto(
    val name: String,
    val parent: CreateCategoryParentDto,
    val storeId: String
) {
    data class CreateCategoryParentDto(
        val id: String,
        val name: String
    )
}