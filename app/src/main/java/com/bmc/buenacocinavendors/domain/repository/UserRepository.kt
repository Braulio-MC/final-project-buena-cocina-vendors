package com.bmc.buenacocinavendors.domain.repository

import com.auth0.android.result.UserProfile
import com.bmc.buenacocinavendors.data.network.service.UserService
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.AuthError
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserProfile(): Result<UserProfile, AuthError> {
        return userService.getUserProfile()
    }

    suspend fun getAccessToken(): Result<String, AuthError> {
        return userService.getAccessToken()
    }


    suspend fun getUserId(): Result<String, AuthError> {
        return userService.getUserId()
    }
}
