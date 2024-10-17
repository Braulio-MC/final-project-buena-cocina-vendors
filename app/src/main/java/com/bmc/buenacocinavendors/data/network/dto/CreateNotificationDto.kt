package com.bmc.buenacocinavendors.data.network.dto

data class CreateNotificationDto(
    val notification: CreateInnerNotificationDto,
    val data: HashMap<String, String>
) {
    data class CreateInnerNotificationDto(
        val title: String,
        val body: String,
    )
}