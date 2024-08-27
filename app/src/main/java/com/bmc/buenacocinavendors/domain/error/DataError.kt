package com.bmc.buenacocinavendors.domain.error

sealed interface DataError : BaseError {
    enum class NetworkError: DataError {
        SERVER_UNAVAILABLE,
        NOT_FOUND,
        BAD_REQUEST,
        UNAUTHORIZED,
        UNPROCESSABLE_CONTENT,
        INTERNAL_SERVER_ERROR,
        UNKNOWN
    }

    enum class LocalError : DataError {
        NOT_FOUND,
        FILE_NOT_FOUND,
        CREATION_FAILED,
        DELETION_FAILED,
        RESOURCE_ALREADY_CREATED,
        UNKNOWN
    }
}