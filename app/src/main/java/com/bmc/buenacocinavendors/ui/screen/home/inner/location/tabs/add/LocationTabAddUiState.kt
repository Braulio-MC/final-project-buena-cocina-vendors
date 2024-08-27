package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.add

import com.bmc.buenacocinavendors.domain.UiText

data class LocationTabAddUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val storeId: String = "" // managed by view model
)
