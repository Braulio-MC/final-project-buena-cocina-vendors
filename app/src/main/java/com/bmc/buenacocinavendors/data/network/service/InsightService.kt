package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.data.network.model.InsightCalculateSalesByDayOfWeekResultNetwork
import com.bmc.buenacocinavendors.data.network.model.InsightTopLocationResultNetwork
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface InsightService {
    @GET("/insights/get-top-locations-on-map")
    suspend fun getTopLocationsOnMap(
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): ApiResponse<InsightTopLocationResultNetwork>

    @GET("/insights/calculate-sales-by-day-of-week")
    suspend fun calculateSalesByDayOfWeek(
        @Query("store_id") storeId: String,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): ApiResponse<InsightCalculateSalesByDayOfWeekResultNetwork>
}