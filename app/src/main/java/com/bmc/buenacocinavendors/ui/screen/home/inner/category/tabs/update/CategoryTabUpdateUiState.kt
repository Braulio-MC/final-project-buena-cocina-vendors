package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.CategoryDomain

data class CategoryTabUpdateUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val currentCategoryUpdate: CategoryDomain? = null,
    val currentCategoryUpdateError: UiText? = null,
    val currentParent: CategoryDomain? = null,
    val storeId: String = "" // managed by view model
)