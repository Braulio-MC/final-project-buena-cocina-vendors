package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.add

sealed class LocationTabAddIntent {
    data class NameChanged(val name: String) : LocationTabAddIntent()
    data class DescriptionChanged(val description: String) : LocationTabAddIntent()
    data object Submit : LocationTabAddIntent()
}