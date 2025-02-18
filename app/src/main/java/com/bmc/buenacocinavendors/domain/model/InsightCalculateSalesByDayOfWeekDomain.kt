package com.bmc.buenacocinavendors.domain.model

import java.time.LocalDate

data class InsightCalculateSalesByDayOfWeekDomain(
    val dayDate: LocalDate,
    val dailyOrderCount: Int,
    val dailyProductCount: Int,
    val dailyRevenue: Double
)
