package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import javax.inject.Inject

class ValidateDiscount @Inject constructor() {
    operator fun invoke(discount: DiscountDomain?): Result<Unit, FormError.DiscountError> {
        if (discount == null) {
            return Result.Error(FormError.DiscountError.NOT_A_VALID_DISCOUNT)
        }
        return Result.Success(Unit)
    }
}