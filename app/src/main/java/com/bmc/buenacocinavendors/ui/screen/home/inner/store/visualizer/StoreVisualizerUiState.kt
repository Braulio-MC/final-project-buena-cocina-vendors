package com.bmc.buenacocinavendors.ui.screen.home.inner.store.visualizer

import com.bmc.buenacocinavendors.domain.model.StoreDomain

data class StoreVisualizerUiState(
    val isLoading: Boolean = false,
    val store: StoreDomain? = null
)
