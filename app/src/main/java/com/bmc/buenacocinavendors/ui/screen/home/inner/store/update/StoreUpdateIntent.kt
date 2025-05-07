package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import android.net.Uri

sealed class StoreUpdateIntent {
    data class NameChanged(val name: String) : StoreUpdateIntent()
    data class DescriptionChanged(val description: String) : StoreUpdateIntent()
    data class EmailChanged(val email: String) : StoreUpdateIntent()
    data class PhoneChanged(val phone: String) : StoreUpdateIntent()
    data class ImageChanged(val image: Uri?) : StoreUpdateIntent()
    data class StartTimeChanged(val hour: Int, val minute: Int) : StoreUpdateIntent()
    data class EndTimeChanged(val hour: Int, val minute: Int) : StoreUpdateIntent()
    data object Submit : StoreUpdateIntent()
}