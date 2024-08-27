package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update

import com.bmc.buenacocinavendors.domain.model.CategoryDomain

sealed class CategoryTabUpdateIntent {
    data class NameChanged(val name: String) : CategoryTabUpdateIntent()
    data class CategoryUpdateChanged(val category: CategoryDomain? = null) : CategoryTabUpdateIntent()
    data class ParentChanged(val parent: CategoryDomain? = null) : CategoryTabUpdateIntent()
    data object Submit : CategoryTabUpdateIntent()
}