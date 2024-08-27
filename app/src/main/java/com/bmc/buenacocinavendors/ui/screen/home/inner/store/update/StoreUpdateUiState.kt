package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import android.net.Uri
import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.StoreDomain

data class StoreUpdateUiState(
    val isLoadingResources: Boolean = false, // Maybe unnecessary
    val isWaitingForResult: Boolean = false,
    val store: StoreDomain? = null,
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
    val userId: String = "" // managed by view model
)
