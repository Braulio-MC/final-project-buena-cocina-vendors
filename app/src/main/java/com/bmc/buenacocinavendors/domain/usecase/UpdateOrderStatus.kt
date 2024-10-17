package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.data.network.dto.UpdateOrderDto
import com.bmc.buenacocinavendors.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderStatus @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(
        orderId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val dto = UpdateOrderDto(status)
        orderRepository.update(orderId, dto, onSuccess, onFailure)
    }
}