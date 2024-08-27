package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.LOCATION_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.LOCATION_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateLocationName @Inject constructor() {
    operator fun invoke(name: String): Result<Unit, FormError.LocationNameError> {
        if (name.isBlank()) {
            return Result.Error(FormError.LocationNameError.NAME_IS_BLANK)
        }
        if (name.length < LOCATION_MINIMUM_NAME_LENGTH) {
            return Result.Error(FormError.LocationNameError.NAME_TOO_SHORT)
        }
        if (name.length > LOCATION_MAXIMUM_NAME_LENGTH) {
            return Result.Error(FormError.LocationNameError.NAME_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}