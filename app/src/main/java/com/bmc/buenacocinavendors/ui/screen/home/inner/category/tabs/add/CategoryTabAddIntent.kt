package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

sealed class CategoryTabAddIntent {
    data class NameChanged(val name: String) : CategoryTabAddIntent()
    data object Submit : CategoryTabAddIntent()
}