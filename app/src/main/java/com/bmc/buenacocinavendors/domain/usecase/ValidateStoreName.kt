package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateStoreName @Inject constructor() {
    operator fun invoke(name: String): Result<Unit, FormError.StoreNameError> {
        if (name.isBlank()) {
            return Result.Error(FormError.StoreNameError.NAME_IS_BLANK)
        }
        if (name.length < STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH) {
            return Result.Error(FormError.StoreNameError.NAME_TOO_SHORT)
        }
        if (name.length > STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH) {
            return Result.Error(FormError.StoreNameError.NAME_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}