package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.general.detailed

import com.bmc.buenacocinavendors.domain.model.CategoryDomain

data class CategoryTabGeneralItemDetailedUiState(
    val isLoading: Boolean = false,
    val category: CategoryDomain? = null,
)