package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.CATEGORY_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.CATEGORY_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateCategoryName @Inject constructor() {
    operator fun invoke(name: String): Result<Unit, FormError.CategoryNameError> {
        if (name.isBlank()) {
            return Result.Error(FormError.CategoryNameError.NAME_IS_BLANK)
        }
        if (name.length < CATEGORY_MINIMUM_NAME_LENGTH) {
            return Result.Error(FormError.CategoryNameError.NAME_TOO_SHORT)
        }
        if (name.length > CATEGORY_MAXIMUM_NAME_LENGTH) {
            return Result.Error(FormError.CategoryNameError.NAME_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}