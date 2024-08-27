package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.update

import com.bmc.buenacocinavendors.domain.model.DiscountDomain

sealed class DiscountTabUpdateIntent {
    data class DiscountUpdateChanged(val discount: DiscountDomain? = null) : DiscountTabUpdateIntent()
    data class NameChanged(val name: String) : DiscountTabUpdateIntent()
    data class PercentageChanged(val percentage: String) : DiscountTabUpdateIntent()
    data class StartDateChanged(val date: Long) : DiscountTabUpdateIntent()
    data class StartTimeChanged(val hour: Int, val minute: Int) : DiscountTabUpdateIntent()
    data class EndDateChanged(val date: Long) : DiscountTabUpdateIntent()
    data class EndTimeChanged(val hour: Int, val minute: Int) : DiscountTabUpdateIntent()
    data object Submit : DiscountTabUpdateIntent()
}