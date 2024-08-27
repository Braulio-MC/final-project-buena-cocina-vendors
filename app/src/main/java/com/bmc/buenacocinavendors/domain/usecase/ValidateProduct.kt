package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import javax.inject.Inject

class ValidateProduct @Inject constructor() {
    operator fun invoke(product: ProductDomain?): Result<Unit, FormError.ProductError> {
        if (product == null) {
            return Result.Error(FormError.ProductError.NOT_A_VALID_PRODUCT)
        }
        return Result.Success(Unit)
    }
}