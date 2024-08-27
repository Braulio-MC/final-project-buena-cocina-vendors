package com.bmc.buenacocinavendors.domain.usecase

import android.net.Uri
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import javax.inject.Inject

class ValidateImage @Inject constructor() {
    operator fun invoke(image: Uri?): Result<Unit, FormError.ImagerError> {
        if (image == null) {
            return Result.Error(FormError.ImagerError.NOT_VALID_IMAGE)
        }
        return Result.Success(Unit)
    }
}