package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.API_URL
import com.bmc.buenacocinavendors.data.network.dto.AlgoliaGetSecuredSearchApiKeyDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AlgoliaTokenService {
    @POST("${API_URL}/get-secured-search-Key")
    suspend fun requestScopedToken(
        @Header("Authorization") authorization: String,
        @Body request: AlgoliaGetSecuredSearchApiKeyDto
    ): ApiResponse<String>
}