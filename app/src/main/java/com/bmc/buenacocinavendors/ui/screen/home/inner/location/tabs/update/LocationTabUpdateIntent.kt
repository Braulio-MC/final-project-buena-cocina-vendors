package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update

import com.bmc.buenacocinavendors.domain.model.LocationDomain

sealed class LocationTabUpdateIntent {
    data class NameChanged(val name: String) : LocationTabUpdateIntent()
    data class DescriptionChanged(val description: String) : LocationTabUpdateIntent()
    data class LocationUpdateChanged(val location: LocationDomain? = null) :
        LocationTabUpdateIntent()

    data object Submit : LocationTabUpdateIntent()
}
