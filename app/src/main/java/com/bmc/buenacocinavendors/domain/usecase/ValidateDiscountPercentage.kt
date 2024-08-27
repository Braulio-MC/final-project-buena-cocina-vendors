package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.DISCOUNT_MAXIMUM_PERCENTAGE
import com.bmc.buenacocinavendors.core.DISCOUNT_MINIMUM_PERCENTAGE
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import javax.inject.Inject

class ValidateDiscountPercentage @Inject constructor() {
    operator fun invoke(percentage: String): Result<Unit, FormError.DiscountPercentageError> {
        if (percentage.isBlank()) {
            return Result.Error(FormError.DiscountPercentageError.PERCENTAGE_IS_BLANK)
        }
        try {
            val p = percentage.toDouble()
            if (p < DISCOUNT_MINIMUM_PERCENTAGE) {
                return Result.Error(FormError.DiscountPercentageError.PERCENTAGE_TOO_SHORT)
            }
            if (p > DISCOUNT_MAXIMUM_PERCENTAGE) {
                return Result.Error(FormError.DiscountPercentageError.PERCENTAGE_TOO_LONG)
            }
            return Result.Success(Unit)
        } catch (e: NumberFormatException) {
            return Result.Error(FormError.DiscountPercentageError.NOT_A_VALID_PERCENTAGE)
        }
    }
}