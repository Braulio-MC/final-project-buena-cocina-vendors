package com.bmc.buenacocinavendors.domain.usecase

import android.util.Patterns
import com.bmc.buenacocinavendors.core.MAXIMUM_EMAIL_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): Result<Unit, FormError.EmailError> {
        if (email.isBlank()) {
            return Result.Error(FormError.EmailError.EMAIL_IS_BLANK)
        }
        if (email.length > MAXIMUM_EMAIL_LENGTH) {
            return Result.Error(FormError.EmailError.EMAIL_TOO_LONG)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error(FormError.EmailError.NOT_A_VALID_EMAIL)
        }
        return Result.Success(Unit)
    }
}