package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.update

import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import java.time.LocalDateTime
import java.time.LocalTime

data class DiscountTabUpdateUiState(
    val isWaitingForResult: Boolean = false,
    val discountUpdate: DiscountDomain? = null,
    val discountUpdateError: UiText? = null,
    val name: String = "",
    val nameError: UiText? = null,
    val percentage: String = "",
    val percentageError: UiText? = null,
    val startDate: LocalDateTime? = null,
    val startTime: LocalTime? = null,
    val endDate: LocalDateTime? = null,
    val endTime: LocalTime? = null,
    val dateConsistencyError: UiText? = null,
    val storeId: String = "" // managed by the view model
)