package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError.StoreTimeError
import javax.inject.Inject

class ValidateTime @Inject constructor() {

    operator fun invoke(time: String): Result<Unit, StoreTimeError> {

        // Preprocesar el input
        val formattedTime = formatUserTimeInput(time) ?: return Result.Error(StoreTimeError.NOT_VALID_TIME_FORMAT)

        // Validar formato correcto HH:mm
        return if (formattedTime.matches(Regex("^(\\d|[01]\\d|2[0-3]):([0-5]\\d)\$"))) {
            Result.Success(Unit)
        } else {
            Result.Error(StoreTimeError.NOT_VALID_TIME_FORMAT)
        }
    }

    private fun formatUserTimeInput(input: String): String? {
        val clean = input.trim()

        // Si ya está en formato HH:mm, regresar igual
        if (clean.matches(Regex("^\\d{1,2}:\\d{2}\$"))) return clean

        // Si tiene solo 3 o 4 dígitos → intentar formatear
        if (clean.length == 3) {
            val hour = clean.substring(0, 1)
            val minute = clean.substring(1, 3)
            return "$hour:$minute"
        }
        if (clean.length == 4) {
            val hour = clean.substring(0, 2)
            val minute = clean.substring(2, 4)
            return "$hour:$minute"
        }

        // No se puede formatear → null
        return null
    }
}

