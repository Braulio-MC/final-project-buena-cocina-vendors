package com.bmc.buenacocinavendors.data.network.service

import android.util.Log
import com.bmc.buenacocinavendors.di.AppDispatcher
import com.bmc.buenacocinavendors.di.AppDispatchers
import com.bmc.buenacocinavendors.domain.repository.TokenRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    @AppDispatcher(AppDispatchers.IO)
    lateinit var ioDispatcher: CoroutineDispatcher

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(ioDispatcher).launch {
            tokenRepository.create(
                token = token,
                onSuccess = { data ->
                    Log.d("PushNotificationService", data.toString())
                },
                onFailure = { e ->
                    Log.d("PushNotificationService", e.toString())
                }
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}