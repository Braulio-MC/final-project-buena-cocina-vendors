package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.detailed

import com.bmc.buenacocinavendors.domain.model.DiscountDomain

data class DiscountTabVisualizerItemDetailedUiState(
    val isLoading: Boolean = false,
    val discount: DiscountDomain? = null
)
