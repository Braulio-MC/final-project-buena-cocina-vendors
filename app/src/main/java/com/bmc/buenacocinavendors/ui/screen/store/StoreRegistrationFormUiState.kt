package com.bmc.buenacocinavendors.ui.screen.store

import android.net.Uri
import com.bmc.buenacocinavendors.domain.UiText

data class StoreRegistrationFormUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val phoneNumber: String = "",
    val phoneNumberError: UiText? = null,
    val image: Uri? = null,
    val imageError: UiText? = null,
    val startTime: Pair<Int, Int> = 0 to 0,
    val endTime: Pair<Int, Int> = 0 to 0,
    val userId: String = "" // managed by view model
)
