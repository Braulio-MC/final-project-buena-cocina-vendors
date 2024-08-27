package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.PRODUCT_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import javax.inject.Inject

class ValidateProductName @Inject constructor() {
    operator fun invoke(name: String): Result<Unit, FormError.ProductNameError> {
        if (name.isBlank()) {
            return Result.Error(FormError.ProductNameError.NAME_IS_BLANK)
        }
        if (name.length < PRODUCT_MINIMUM_NAME_LENGTH) {
            return Result.Error(FormError.ProductNameError.NAME_TOO_SHORT)
        }
        if (name.length > PRODUCT_MAXIMUM_NAME_LENGTH) {
            return Result.Error(FormError.ProductNameError.NAME_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}