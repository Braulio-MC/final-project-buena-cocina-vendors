package com.bmc.buenacocinavendors.ui.screen.home

import com.bmc.buenacocinavendors.domain.model.StoreDomain

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingWeeklySales: Boolean = false,
    val store: StoreDomain? = null
)
