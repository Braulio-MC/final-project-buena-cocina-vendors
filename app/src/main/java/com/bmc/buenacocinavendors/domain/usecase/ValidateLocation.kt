package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import javax.inject.Inject

class ValidateLocation @Inject constructor() {
    operator fun invoke(location: LocationDomain?): Result<Unit, FormError.LocationError> {
        if (location == null) {
            return Result.Error(FormError.LocationError.NOT_A_VALID_LOCATION)
        }
        return Result.Success(Unit)
    }
}