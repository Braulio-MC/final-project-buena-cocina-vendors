package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.LOCATION_MAXIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.LOCATION_MINIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import javax.inject.Inject

class ValidateLocationDescription @Inject constructor() {
    operator fun invoke(description: String): Result<Unit, FormError.LocationDescriptionError> {
        if (description.isBlank()) {
            return Result.Error(FormError.LocationDescriptionError.DESCRIPTION_IS_BLANK)
        }
        if (description.length < LOCATION_MINIMUM_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.LocationDescriptionError.DESCRIPTION_TOO_SHORT)
        }
        if (description.length > LOCATION_MAXIMUM_DESCRIPTION_LENGTH) {
            return Result.Error(FormError.LocationDescriptionError.DESCRIPTION_TOO_LONG)
        }
        return Result.Success(Unit)
    }
}