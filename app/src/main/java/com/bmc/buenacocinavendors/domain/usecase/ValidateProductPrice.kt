package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_PRICE
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateProductPrice @Inject constructor() {
    operator fun invoke(price: String): Result<Unit, FormError.ProductPriceError> {
        if (price.isBlank()) {
            return Result.Error(FormError.ProductPriceError.PRICE_IS_BLANK)
        }
        try {
            val p = price.toDouble()
            if (p < PRODUCT_MINIMUM_PRICE) {
                return Result.Error(FormError.ProductPriceError.PRICE_TOO_SHORT)
            }
            return Result.Success(Unit)
        } catch (e: NumberFormatException) {
            return Result.Error(FormError.ProductPriceError.NOT_A_VALID_PRICE)
        }
    }
}