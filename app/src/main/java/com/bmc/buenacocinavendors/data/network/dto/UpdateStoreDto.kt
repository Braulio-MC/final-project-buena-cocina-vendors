package com.bmc.buenacocinavendors.data.network.dto

import android.net.Uri

data class UpdateStoreDto(
    val name: String,
    val description: String,
    val email: String,
    val phoneNumber: String,
    val startTime: UpdateStoreWorkingHoursDto,
    val endTime: UpdateStoreWorkingHoursDto,
    val userId: String,
    val image: Uri?,
    val oldPath: String?
) {
    data class UpdateStoreWorkingHoursDto(
        val hour: Int,
        val minute: Int
    )
}
