package com.bmc.buenacocinavendors.ui.screen.order.detailed

import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.domain.UiText
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain

data class DetailedOrderUiResultState(
    val isWaitingForChannelResult: Boolean = false,
    val isWaitingForStatusResult: Boolean = false,
    val status: OrderStatus = OrderStatus.UNASSIGNED,
    val statusError: UiText? = null
)

data class DetailedOrderUiState(
    val isLoadingResources: Boolean = false,
    val order: OrderDomain? = null,
    val lines: List<OrderLineDomain> = emptyList()
)