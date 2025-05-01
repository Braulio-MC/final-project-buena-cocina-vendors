package com.bmc.buenacocinavendors.ui.screen.store

import android.net.Uri

sealed class StoreRegistrationFormIntent {
    data class NameChanged(val name: String) : StoreRegistrationFormIntent()
    data class DescriptionChanged(val description: String) : StoreRegistrationFormIntent()
    data class EmailChanged(val email: String) : StoreRegistrationFormIntent()
    data class PhoneChanged(val phone: String) : StoreRegistrationFormIntent()
    data class ImageChanged(val image: Uri?) : StoreRegistrationFormIntent()
    data class StartTimeChanged(val startTime: String) : StoreRegistrationFormIntent()
    data class EndTimeChanged(val endTime: String) : StoreRegistrationFormIntent()
    data object Submit : StoreRegistrationFormIntent()
}