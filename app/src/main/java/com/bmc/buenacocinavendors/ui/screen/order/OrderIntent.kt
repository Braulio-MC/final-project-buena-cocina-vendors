package com.bmc.buenacocinavendors.ui.screen.order

sealed class OrderIntent {
    data class UpdateSearchQuery(val searchQuery: String): OrderIntent()
    data object ClearSearch : OrderIntent()
    data object Search : OrderIntent()
}