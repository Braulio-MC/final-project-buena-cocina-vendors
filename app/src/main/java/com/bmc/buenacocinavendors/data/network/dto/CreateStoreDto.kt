package com.bmc.buenacocinavendors.data.network.dto

import android.net.Uri

data class CreateStoreDto(
    val name: String,
    val description: String,
    val email: String,
    val phoneNumber: String,
    val userId: String,
    val image: Uri
)