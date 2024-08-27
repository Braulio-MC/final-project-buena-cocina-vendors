package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_QUANTITY
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateProductQuantity @Inject constructor() {
    operator fun invoke(quantity: String): Result<Unit, FormError.ProductQuantityError> {
        if (quantity.isBlank()) {
            return Result.Error(FormError.ProductQuantityError.QUANTITY_IS_BLANK)
        }
        try {
            val q = quantity.toInt()
            if (q < PRODUCT_MINIMUM_QUANTITY) {
                return Result.Error(FormError.ProductQuantityError.QUANTITY_TOO_SHORT)
            }
            return Result.Success(Unit)
        } catch (e: NumberFormatException) {
            return Result.Error(FormError.ProductQuantityError.NOT_A_VALID_QUANTITY)
        }

    }
}