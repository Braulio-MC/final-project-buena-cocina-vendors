package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete

import com.bmc.buenacocinavendors.domain.model.LocationDomain

sealed class LocationTabDeleteIntent {
    data class LocationDeleteChanged(val location: LocationDomain? = null) : LocationTabDeleteIntent()
    data object Submit : LocationTabDeleteIntent()
}