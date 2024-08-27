package com.bmc.buenacocinavendors.data.network.handler

import com.bmc.buenacocinavendors.domain.error.DataError
import java.io.IOException

// Investigate how to handle exceptions correctly
fun handleApiException(e: Throwable): DataError {
    return when (e) {
        is IOException -> {
            DataError.LocalError.UNKNOWN
        }

        else -> DataError.NetworkError.UNKNOWN
    }
}