package com.bmc.buenacocinavendors.domain

import com.bmc.buenacocinavendors.domain.error.BaseError

typealias RootError = BaseError

sealed interface Result<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E: RootError>(val error: E) : Result<D, E>
}