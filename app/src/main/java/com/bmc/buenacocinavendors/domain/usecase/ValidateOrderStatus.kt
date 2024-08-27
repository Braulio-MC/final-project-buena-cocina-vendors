package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import javax.inject.Inject

class ValidateOrderStatus @Inject constructor() {
    operator fun invoke(status: OrderStatus): Result<Unit, FormError.OrderStatusError> {
        if (OrderStatus.entries.contains(status)) {
            return Result.Success(Unit)
        }
        return Result.Error(FormError.OrderStatusError.NOT_A_VALID_ORDER_STATUS)
    }
}