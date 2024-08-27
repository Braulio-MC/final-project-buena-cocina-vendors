package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

import com.bmc.buenacocinavendors.domain.model.CategoryDomain

sealed class CategoryTabAddIntent {
    data class NameChanged(val name: String) : CategoryTabAddIntent()
    data class ParentChanged(val parent: CategoryDomain? = null) : CategoryTabAddIntent()
    data object Submit : CategoryTabAddIntent()
}