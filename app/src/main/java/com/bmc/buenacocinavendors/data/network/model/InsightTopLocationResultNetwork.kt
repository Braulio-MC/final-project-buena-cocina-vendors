package com.bmc.buenacocinavendors.data.network.model

import com.google.gson.annotations.SerializedName

// From PyApi
data class InsightTopLocationResultNetwork(
    @SerializedName("data") val points: List<InsightTopLocationNetwork>
)
