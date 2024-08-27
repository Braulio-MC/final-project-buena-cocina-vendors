package com.bmc.buenacocinavendors.domain.usecase

import android.util.Patterns
import com.bmc.buenacocinavendors.core.MAXIMUM_PHONE_NUMBER_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidatePhoneNumber @Inject constructor() {
    operator fun invoke(phoneNumber: String): Result<Unit, FormError.PhoneNumberError> {
        if (phoneNumber.isBlank()) {
            return Result.Error(FormError.PhoneNumberError.PHONE_NUMBER_IS_BLANK)
        }
        if (phoneNumber.length < MAXIMUM_PHONE_NUMBER_LENGTH) {
            return Result.Error(FormError.PhoneNumberError.PHONE_NUMBER_TOO_SHORT)
        }
        if (phoneNumber.length > MAXIMUM_PHONE_NUMBER_LENGTH) {
            return Result.Error(FormError.PhoneNumberError.PHONE_NUMBER_TOO_LONG)
        }
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            return Result.Error(FormError.PhoneNumberError.NOT_A_VALID_PHONE_NUMBER)
        }
        return Result.Success(Unit)
    }
}