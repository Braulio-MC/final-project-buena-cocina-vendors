package com.bmc.buenacocinavendors.domain.model

import com.google.maps.android.heatmaps.WeightedLatLng

// From PyApi
data class InsightTopLocationDomain(
    val geohash: String,
    val geopoint: WeightedLatLng
)