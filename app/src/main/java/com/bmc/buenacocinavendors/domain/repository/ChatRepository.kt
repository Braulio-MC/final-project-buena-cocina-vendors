package com.bmc.buenacocinavendors.domain.repository

import com.bmc.buenacocinavendors.data.network.model.GetStreamUserCredentials
import com.bmc.buenacocinavendors.data.network.service.ChatService
import io.getstream.result.Error
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatService: ChatService
) {
    suspend fun connectUser(
        credentials: GetStreamUserCredentials,
        onSuccess: () -> Unit,
        onFailure: (Error) -> Unit
    ) {
        chatService.connectUser(credentials, onSuccess, onFailure)
    }

    suspend fun disconnectUser() {
        chatService.disconnectUser()
    }
}