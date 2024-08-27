package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.CategoryDomain

data class CategoryTabAddUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val currentParent: CategoryDomain? = null,
    val storeId: String = "" // managed by view model
)