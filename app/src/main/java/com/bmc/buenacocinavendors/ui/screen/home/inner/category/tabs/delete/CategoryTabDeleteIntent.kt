package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.delete

import com.bmc.buenacocinavendors.domain.model.CategoryDomain

sealed class CategoryTabDeleteIntent {
    data class CategoryDeleteChanged(val category: CategoryDomain? = null) : CategoryTabDeleteIntent()
    data object Submit : CategoryTabDeleteIntent()
}
