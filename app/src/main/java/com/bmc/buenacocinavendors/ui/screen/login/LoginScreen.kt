package com.bmc.buenacocinavendors.ui.screen.login

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginButton: (Boolean) -> Unit
) {
    val netState = viewModel.netState.collectAsStateWithLifecycle()

    var isLoginButtonEnabled by remember {
        mutableStateOf(true)
    }

    LoginScreenContent(
        windowSizeClass = windowSizeClass,
        netState = netState.value,
        onLoginButton = onLoginButton,
        isLoginButtonEnabled = isLoginButtonEnabled,
        updateEnableLoginButton = { enabled ->
            isLoginButtonEnabled = enabled
        },
        onStartLogin = viewModel::startLogin
    )
}

@Composable
fun LoginScreenContent(
    windowSizeClass: WindowSizeClass,
    netState: NetworkStatus,
    onLoginButton: (Boolean) -> Unit,
    isLoginButtonEnabled: Boolean,
    updateEnableLoginButton: (Boolean) -> Unit,
    onStartLogin: (Context, () -> Unit, () -> Unit) -> Unit
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
        LoginScreenExpanded(
            netState = netState,
            onLoginButton = onLoginButton,
            isLoginButtonEnabled = isLoginButtonEnabled,
            updateEnableLoginButton = updateEnableLoginButton,
            onStartLogin = onStartLogin
        )
    } else {
        LoginScreenCompactMedium(
            netState = netState,
            onLoginButton = onLoginButton,
            isLoginButtonEnabled = isLoginButtonEnabled,
            updateEnableLoginButton = updateEnableLoginButton,
            onStartLogin = onStartLogin
        )
    }
}