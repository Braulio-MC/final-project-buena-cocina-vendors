package com.bmc.buenacocinavendors.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bmc.buenacocinavendors.di.AppDispatcher
import com.bmc.buenacocinavendors.di.AppDispatchers
import com.bmc.buenacocinavendors.domain.repository.TokenRepository
import com.bmc.buenacocinavendors.ui.navigation.graph.NavigationGraph
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var messaging: FirebaseMessaging

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    @AppDispatcher(AppDispatchers.IO)
    lateinit var ioDispatcher: CoroutineDispatcher

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
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
                        onFinishActivity = {
                            finish()
                        },
                        onHasStore = { storeId ->
                            splashScreen.setKeepOnScreenCondition { true }
                            messaging.token.addOnSuccessListener { token ->
                                tokenRepository.exists(
                                    storeId = storeId,
                                    token = token,
                                    onSuccess = { exists ->
                                        if (exists) {
                                            splashScreen.setKeepOnScreenCondition { false }
                                        } else {
                                            processTokenDoesNotExist(
                                                storeId = storeId,
                                                token = token,
                                                onSuccess = {
                                                    splashScreen.setKeepOnScreenCondition { false }
                                                },
                                                onFailure = { e ->
                                                    splashScreen.setKeepOnScreenCondition { false }
                                                    e.printStackTrace()
                                                }
                                            )
                                        }
                                    },
                                    onFailure = { e ->
                                        splashScreen.setKeepOnScreenCondition { false }
                                        e.printStackTrace()
                                    }
                                )
                            }.addOnFailureListener { e ->
                                splashScreen.setKeepOnScreenCondition { false }
                                e.printStackTrace()
                            }
                        }
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

    private fun processTokenDoesNotExist(
        storeId: String,
        token: String,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        CoroutineScope(ioDispatcher).launch {
            tokenRepository.create(
                storeId = storeId,
                token = token,
                onSuccess = {
                    onSuccess(it)
                },
                onFailure = { e ->
                    onFailure(e)
                }
            )
        }
    }
}
