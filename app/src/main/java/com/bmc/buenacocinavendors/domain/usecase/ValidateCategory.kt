package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import javax.inject.Inject

class ValidateCategory @Inject constructor() {
    operator fun invoke(category: CategoryDomain?): Result<Unit, FormError.CategoryError> {
        if (category == null) {
            return Result.Error(FormError.CategoryError.NOT_A_VALID_CATEGORY)
        }
        return Result.Success(Unit)
    }
}