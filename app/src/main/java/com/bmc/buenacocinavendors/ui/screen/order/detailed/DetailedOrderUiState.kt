package com.bmc.buenacocinavendors.ui.screen.order.detailed

import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.InsightTopLocationDomain
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal

data class DetailedOrderUiState(
    val isLoadingOrder: Boolean = false,
    val isLoadingOrderLines: Boolean = false,
    val isLoadingUserLocation: Boolean = false,
    val isLoadingTopLocationsOnMap: Boolean = false,
    val isCalculatingOrderTotal: Boolean = false,
    val isWaitingForChannelResult: Boolean = false,
    val isWaitingForStatusResult: Boolean = false,
    val status: OrderStatus = OrderStatus.UNASSIGNED,
    val statusError: UiText? = null,
    val orderTotal: BigDecimal = BigDecimal.ZERO,
    val order: OrderDomain? = null,
    val lines: List<OrderLineDomain> = emptyList(),
    val cuceiCenterOnMap: Pair<String, LatLng>? = null,
    val cuceiAreaBoundsOnMap: List<Pair<String, LatLng>>? = null,
    val userLocation: LatLng? = null,
    val topLocationsOnMap: List<InsightTopLocationDomain> = emptyList()
)