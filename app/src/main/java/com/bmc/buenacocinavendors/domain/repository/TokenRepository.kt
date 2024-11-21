package com.bmc.buenacocinavendors.domain.repository

import com.bmc.buenacocinavendors.data.network.service.TokenService
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val tokenService: TokenService
) {
    suspend fun create(
        storeId: String = "",
        token: String? = null,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        tokenService.create(
            storeId = storeId,
            token = token,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    suspend fun remove(
        token: String? = null,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        tokenService.remove(
            token = token,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}