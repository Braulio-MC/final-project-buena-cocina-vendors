package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add

import com.bmc.buenacocinavendors.domain.UiText
import java.time.LocalDateTime
import java.time.LocalTime

data class DiscountTabAddUiState(
    val isWaitingForResult: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val percentage: String = "",
    val percentageError: UiText? = null,
    val startDate: LocalDateTime? = null,
    val startTime: LocalTime? = null,
    val endDate: LocalDateTime? = null,
    val endTime: LocalTime? = null,
    val dateConsistencyError: UiText? = null,
    val storeId: String = "" // managed by view model
)
