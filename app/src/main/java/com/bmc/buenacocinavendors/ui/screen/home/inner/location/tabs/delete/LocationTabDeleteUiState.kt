package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.LocationDomain

data class LocationTabDeleteUiState(
    val isWaitingForResult: Boolean = false,
    val locationDelete: LocationDomain? = null,
    val locationDeleteError: UiText? = null
)
