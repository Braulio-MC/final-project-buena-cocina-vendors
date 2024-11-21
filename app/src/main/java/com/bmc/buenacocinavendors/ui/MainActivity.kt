package com.bmc.buenacocinavendors.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bmc.buenacocinavendors.data.preferences.PreferencesService
import com.bmc.buenacocinavendors.domain.repository.ChatRepository
import com.bmc.buenacocinavendors.domain.repository.TokenRepository
import com.bmc.buenacocinavendors.ui.navigation.graph.NavigationGraph
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.querysort.QuerySortByField
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var chatClient: ChatClient

    @Inject
    lateinit var preferencesService: PreferencesService

    @Inject
    lateinit var getStreamChatRepository: ChatRepository

    private val channelViewModelFactory by lazy {
        ChannelViewModelFactory(
            chatClient = chatClient,
            QuerySortByField.descByName("last_updated")
        )
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        processFCMTokenCreation()
        connectUserToGetStream()
        FirebaseApp.initializeApp(this)
        setContent {
            BuenaCocinaVendorsTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSizeClass = calculateWindowSizeClass(activity = this)
                    NavigationGraph(
                        windowSizeClass = windowSizeClass,
                        channelViewModelFactory = channelViewModelFactory,
                        onFinishActivity = { finish() },
                        onHasStore = { storeId, _ -> processFCMTokenCreation(storeId) }
                    )
                }
            }
        }
        splashScreen.setKeepOnScreenCondition { false }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = checkSelfPermission(
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun connectUserToGetStream() {
        lifecycleScope.launch {
            val credentials = preferencesService.getUserCredentials()
            val result = getStreamChatRepository.connectUser(credentials)
            result.onSuccess { }
            result.onFailure { e ->
                Log.e("MainActivity", "Error on connectUser to GetStream: ${e.message}")
            }
        }
    }

    private fun processFCMTokenCreation(storeId: String = "") {
        lifecycleScope.launch {
            tokenRepository.create(
                storeId = storeId,
                onSuccess = { Log.d("MainActivity", "FCM token created") },
                onFailure = { e -> e.printStackTrace() }
            )
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}