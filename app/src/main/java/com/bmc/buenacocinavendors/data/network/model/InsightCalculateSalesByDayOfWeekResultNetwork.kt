package com.bmc.buenacocinavendors.data.network.model

import com.google.gson.annotations.SerializedName

data class InsightCalculateSalesByDayOfWeekResultNetwork(
    @SerializedName("data") val sales: List<InsightCalculateSalesByDayOfWeekNetwork>
)
