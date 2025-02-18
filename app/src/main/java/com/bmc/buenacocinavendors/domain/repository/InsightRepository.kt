package com.bmc.buenacocinavendors.domain.repository

import com.bmc.buenacocinavendors.data.network.handler.handleApiException
import com.bmc.buenacocinavendors.data.network.handler.handleApiFailure
import com.bmc.buenacocinavendors.data.network.service.InsightService
import com.bmc.buenacocinavendors.domain.error.DataError
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.mapper.asDomain
import com.bmc.buenacocinavendors.domain.model.InsightCalculateSalesByDayOfWeekDomain
import com.bmc.buenacocinavendors.domain.model.InsightTopLocationDomain
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.statusCode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class InsightRepository @Inject constructor(
    private val insightService: InsightService
) {
    suspend fun getTopLocationsOnMap(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): Result<List<InsightTopLocationDomain>, DataError> {
        return when (val response = insightService.getTopLocationsOnMap(
            startDate?.format(DateTimeFormatter.ISO_LOCAL_DATE),
            endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        )) {
            is ApiResponse.Failure.Error -> {
                Result.Error(handleApiFailure(response.statusCode))
            }

            is ApiResponse.Failure.Exception -> {
                Result.Error(handleApiException(response.throwable))
            }

            is ApiResponse.Success -> {
                val domain = response.data.points.map { point ->
                    point.asDomain()
                }
                Result.Success(domain)
            }
        }
    }

    suspend fun calculateSalesByDayOfWeek(
        storeId: String,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): Result<List<InsightCalculateSalesByDayOfWeekDomain>, DataError> {
        return when (val response = insightService.calculateSalesByDayOfWeek(
            storeId,
            startDate?.format(DateTimeFormatter.ISO_LOCAL_DATE),
            endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        )) {
            is ApiResponse.Failure.Error -> {
                Result.Error(handleApiFailure(response.statusCode))
            }

            is ApiResponse.Failure.Exception -> {
                Result.Error(handleApiException(response.throwable))
            }

            is ApiResponse.Success -> {
                val domain = response.data.sales.map { sale ->
                    sale.asDomain()
                }
                Result.Success(domain)
            }
        }
    }
}