package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.DISCOUNT_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.DISCOUNT_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateDiscountName @Inject constructor() {
    operator fun invoke(name: String): Result<Unit, FormError.DiscountNameError> {
        if (name.isBlank()) {
            return Result.Error(FormError.DiscountNameError.NAME_IS_BLANK)
        }
        if (name.length < DISCOUNT_MINIMUM_NAME_LENGTH) {
            return Result.Error(FormError.DiscountNameError.NAME_TOO_SHORT)
        }
        if (name.length > DISCOUNT_MAXIMUM_NAME_LENGTH) {
            return Result.Error(FormError.DiscountNameError.NAME_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}