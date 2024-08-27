package com.bmc.buenacocinavendors.ui.screen.profile

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
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
import com.bmc.buenacocinavendors.ui.viewmodel.ProfileViewModel

/* TODO
    - Improve screen design
*/

@Composable
fun ProfileScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    scrollState: ScrollState = rememberScrollState(),
    onLogoutButton: (Boolean) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val netState by viewModel.netState.collectAsStateWithLifecycle()

    var isLogoutButtonEnabled by remember {
        mutableStateOf(true)
    }

    ProfileScreenContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        netState = netState,
        scrollState = scrollState,
        isLogoutButtonEnabled = isLogoutButtonEnabled,
        onLogoutButtonChanged = { enabled ->
            isLogoutButtonEnabled = enabled
        },
        onStartLogout = viewModel::startLogout,
        onLogoutButton = onLogoutButton
    )
}

@Composable
fun ProfileScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: ProfileUiState,
    netState: NetworkStatus,
    scrollState: ScrollState,
    isLogoutButtonEnabled: Boolean,
    onLogoutButtonChanged: (Boolean) -> Unit,
    onStartLogout: (Context, () -> Unit, () -> Unit) -> Unit,
    onLogoutButton: (Boolean) -> Unit
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
        ProfileScreenExpanded(
            uiState = uiState,
            netState = netState,
            scrollState = scrollState,
            isLogoutButtonEnabled = isLogoutButtonEnabled,
            onLogoutButtonChanged = onLogoutButtonChanged,
            onStartLogout = onStartLogout,
            onLogoutButton = onLogoutButton
        )
    } else {
        ProfileScreenCompactMedium(
            uiState = uiState,
            netState = netState,
            scrollState = scrollState,
            isLogoutButtonEnabled = isLogoutButtonEnabled,
            onLogoutButtonChanged = onLogoutButtonChanged,
            onStartLogout = onStartLogout,
            onLogoutButton = onLogoutButton
        )
    }
}
