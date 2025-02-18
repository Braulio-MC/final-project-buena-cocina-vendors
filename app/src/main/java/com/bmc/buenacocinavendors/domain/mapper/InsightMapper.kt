package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.InsightCalculateSalesByDayOfWeekNetwork
import com.bmc.buenacocinavendors.data.network.model.InsightTopLocationNetwork
import com.bmc.buenacocinavendors.domain.model.InsightCalculateSalesByDayOfWeekDomain
import com.bmc.buenacocinavendors.domain.model.InsightTopLocationDomain
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object InsightMapper {
    fun asDomain(network: InsightTopLocationNetwork): InsightTopLocationDomain {
        return InsightTopLocationDomain(
            geohash = network.geohash,
            geopoint = WeightedLatLng(
                LatLng(network.geopoint.lat, network.geopoint.lng),
                network.intensity.toDouble()
            )
        )
    }

    fun asDomain(network: InsightCalculateSalesByDayOfWeekNetwork): InsightCalculateSalesByDayOfWeekDomain {
        return InsightCalculateSalesByDayOfWeekDomain(
            dayDate = DateUtils.iso8601UTCStringDateToLocalDate(network.dayDate),
            dailyOrderCount = network.dailyOrderCount,
            dailyProductCount = network.dailyProductCount,
            dailyRevenue = network.dailyRevenue
        )
    }

    fun mapSalesDatesToDayOfWeek(
        salesByDayOfWeekDomain: List<InsightCalculateSalesByDayOfWeekDomain>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Pair<String, InsightCalculateSalesByDayOfWeekDomain?>> {
        val salesDates = salesByDayOfWeekDomain.associateBy { it.dayDate }
        return generateSequence(startDate) { date ->
            date.plusDays(1).takeIf { it <= endDate }
        }.map { date ->
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            dayName to salesDates[date]
        }.toList()
    }
}

fun InsightTopLocationNetwork.asDomain() = InsightMapper.asDomain(this)
fun InsightCalculateSalesByDayOfWeekNetwork.asDomain() = InsightMapper.asDomain(this)
fun List<InsightCalculateSalesByDayOfWeekDomain>.mapSalesDatesToDayOfWeek(
    startDate: LocalDate,
    endDate: LocalDate
): List<Pair<String, InsightCalculateSalesByDayOfWeekDomain?>> {
    return InsightMapper.mapSalesDatesToDayOfWeek(this, startDate, endDate)
}