package com.bmc.buenacocinavendors.ui.navigation

sealed class NavigationState {
    data object Loading : NavigationState()
    data object NotAuthenticated : NavigationState()
    data class HasStore(val storeId: String) : NavigationState()
    data object NotStore : NavigationState()
}