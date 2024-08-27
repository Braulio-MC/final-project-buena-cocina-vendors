package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add

sealed class DiscountTabAddIntent {
    data class NameChanged(val name: String) : DiscountTabAddIntent()
    data class PercentageChanged(val percentage: String) : DiscountTabAddIntent()
    data class StartDateChanged(val startDate: Long) : DiscountTabAddIntent()
    data class StartTimeChanged(val hour: Int, val minute: Int) : DiscountTabAddIntent()
    data class EndDateChanged(val endDate: Long) : DiscountTabAddIntent()
    data class EndTimeChanged(val hour: Int, val minute: Int) : DiscountTabAddIntent()
    data object Submit : DiscountTabAddIntent()
}