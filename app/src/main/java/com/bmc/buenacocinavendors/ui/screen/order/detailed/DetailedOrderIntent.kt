package com.bmc.buenacocinavendors.ui.screen.order.detailed

import com.bmc.buenacocinavendors.core.OrderStatus

sealed class DetailedOrderIntent {
    data class ChangeStatus(val status: OrderStatus) : DetailedOrderIntent()
    data object Submit : DetailedOrderIntent()
}