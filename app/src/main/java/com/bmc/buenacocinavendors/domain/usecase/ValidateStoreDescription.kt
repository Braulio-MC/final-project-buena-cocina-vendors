package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateStoreDescription @Inject constructor() {
    operator fun invoke(description: String): Result<Unit, FormError.StoreDescriptionError> {
        if (description.isBlank()) {
            return Result.Error(FormError.StoreDescriptionError.DESCRIPTION_IS_BLANK)
        }
        if (description.length < STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.StoreDescriptionError.DESCRIPTION_TOO_SHORT)
        }
        if (description.length > STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.StoreDescriptionError.DESCRIPTION_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}