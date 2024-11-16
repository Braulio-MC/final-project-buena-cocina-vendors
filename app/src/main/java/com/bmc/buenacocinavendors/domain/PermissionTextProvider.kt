package com.bmc.buenacocinavendors.domain

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}