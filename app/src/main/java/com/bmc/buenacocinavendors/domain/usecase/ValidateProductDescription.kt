package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.PRODUCT_MAXIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateProductDescription @Inject constructor() {
    operator fun invoke(description: String): Result<Unit, FormError.ProductDescriptionError> {
        if (description.isBlank()) {
            return Result.Error(FormError.ProductDescriptionError.DESCRIPTION_IS_BLANK)
        }
        if (description.length < PRODUCT_MINIMUM_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.ProductDescriptionError.DESCRIPTION_TOO_SHORT)
        }
        if (description.length > PRODUCT_MAXIMUM_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.ProductDescriptionError.DESCRIPTION_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}