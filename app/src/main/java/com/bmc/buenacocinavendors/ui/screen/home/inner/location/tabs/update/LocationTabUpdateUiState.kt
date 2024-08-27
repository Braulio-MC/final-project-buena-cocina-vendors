package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.LocationDomain

data class LocationTabUpdateUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val currentLocationUpdate: LocationDomain? = null,
    val currentLocationUpdateError: UiText? = null,
    val storeId: String = "" // managed by view model
)
