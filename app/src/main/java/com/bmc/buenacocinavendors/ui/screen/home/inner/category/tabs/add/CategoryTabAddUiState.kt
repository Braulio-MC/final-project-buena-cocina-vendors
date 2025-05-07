package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

import com.bmc.buenacocinavendors.domain.UiText

data class CategoryTabAddUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val storeId: String = "" // managed by view model
)