package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.delete

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.CategoryDomain

data class CategoryTabDeleteUiState(
    val isWaitingForResult: Boolean = false,
    val categoryDelete: CategoryDomain? = null,
    val categoryDeleteError: UiText? = null
)