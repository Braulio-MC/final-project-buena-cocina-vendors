package com.bmc.buenacocinavendors.domain.repository

import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.core.AlgoliaGetSecuredSearchApiKeyScopes
import com.bmc.buenacocinavendors.data.network.dto.AlgoliaGetSecuredSearchApiKeyDto
import com.bmc.buenacocinavendors.data.network.handler.handleApiException
import com.bmc.buenacocinavendors.data.network.handler.handleApiFailure
import com.bmc.buenacocinavendors.data.network.service.AlgoliaTokenService
import com.bmc.buenacocinavendors.domain.error.DataError
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.statusCode
import javax.inject.Inject

class AlgoliaTokenRepository @Inject constructor(
    private val algoliaTokenService: AlgoliaTokenService
) {
    suspend fun requestScopedToken(
        authorization: String,
        scopeType: AlgoliaGetSecuredSearchApiKeyScopes
    ): Result<String, DataError> {
        val dto = AlgoliaGetSecuredSearchApiKeyDto(scopeType.scope)
        return when (val response = algoliaTokenService.requestScopedToken(authorization, dto)) {
            is ApiResponse.Failure.Error -> {
                Result.Error(handleApiFailure(response.statusCode))
            }

            is ApiResponse.Failure.Exception -> {
                Result.Error(handleApiException(response.throwable))
            }

            is ApiResponse.Success -> {
                Result.Success(response.data)
            }
        }
    }
}