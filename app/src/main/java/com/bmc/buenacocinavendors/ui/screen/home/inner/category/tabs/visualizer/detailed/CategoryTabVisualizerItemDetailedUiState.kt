package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.visualizer.detailed

import com.bmc.buenacocinavendors.domain.model.CategoryDomain

data class CategoryTabVisualizerItemDetailedUiState(
    val isLoading: Boolean = false,
    val category: CategoryDomain? = null,
)