package com.bmc.buenacocinavendors.data.network.model

import com.google.gson.annotations.SerializedName

data class InsightCalculateSalesByDayOfWeekNetwork(
    @SerializedName("day_date") val dayDate: String,
    @SerializedName("daily_order_count") val dailyOrderCount: Int,
    @SerializedName("daily_product_count") val dailyProductCount: Int,
    @SerializedName("daily_revenue") val dailyRevenue: Double
)
