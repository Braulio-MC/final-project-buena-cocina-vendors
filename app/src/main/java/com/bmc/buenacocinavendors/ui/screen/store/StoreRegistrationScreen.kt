package com.bmc.buenacocinavendors.ui.screen.store

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.ui.viewmodel.StoreRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: StoreRegistrationViewModel = hiltViewModel(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onLogoutButton: (Boolean) -> Unit,
    onSuccessfulRegistration: () -> Unit,
    onInformationButton: () -> Unit,
    onBackButton: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onIntent(StoreRegistrationFormIntent.ImageChanged(uri))
        }
    )
    var isLogoutButtonEnabled by remember {
        mutableStateOf(true)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is StoreRegistrationViewModel.ValidationEvent.Success -> {
                    val result =
                        snackbarHostState.showSnackbar(
                            message = "Tienda creada con exito",
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulRegistration()
                    }
                }

                is StoreRegistrationViewModel.ValidationEvent.Failure -> {
                    Log.e("StoreRegistrationScreen", "Error: ${event.error}")
                }
            }
        }
    }

    StoreRegistrationScreenContent(
        windowSizeClass = windowSizeClass,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        snackbarHostState = snackbarHostState,
        uiState = uiState.value,
        isLogoutButtonEnabled = isLogoutButtonEnabled,
        onLogoutButtonChanged = { enabled ->
            isLogoutButtonEnabled = enabled
        },
        onIntent = viewModel::onIntent,
        onPickPhotoButton = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onStartLogout = viewModel::startLogout,
        onLogoutButton = onLogoutButton,
        onInformationButton = onInformationButton,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationScreenContent(
    windowSizeClass: WindowSizeClass,
    scrollBehavior: TopAppBarScrollBehavior,
    scrollState: ScrollState,
    snackbarHostState: SnackbarHostState,
    uiState: StoreRegistrationFormUiState,
    isLogoutButtonEnabled: Boolean,
    onLogoutButtonChanged: (Boolean) -> Unit,
    onIntent: (StoreRegistrationFormIntent) -> Unit,
    onPickPhotoButton: () -> Unit,
    onStartLogout: (Context, () -> Unit, () -> Unit) -> Unit,
    onLogoutButton: (Boolean) -> Unit,
    onInformationButton: () -> Unit,
    onBackButton: () -> Unit
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
        StoreRegistrationScreenExpanded(
            scrollBehavior = scrollBehavior,
            scrollState = scrollState,
            snackbarHostState = snackbarHostState,
            uiState = uiState,
            isLogoutButtonEnabled = isLogoutButtonEnabled,
            onLogoutButtonChanged = onLogoutButtonChanged,
            onIntent = onIntent,
            onPickPhotoButton = onPickPhotoButton,
            onStartLogout = onStartLogout,
            onLogoutButton = onLogoutButton,
            onInformationButton = onInformationButton,
            onBackButton = onBackButton
        )
    } else {
        StoreRegistrationScreenCompactMedium(
            scrollBehavior = scrollBehavior,
            scrollState = scrollState,
            snackbarHostState = snackbarHostState,
            uiState = uiState,
            isLogoutButtonEnabled = isLogoutButtonEnabled,
            onLogoutButtonChanged = onLogoutButtonChanged,
            onIntent = onIntent,
            onPickPhotoButton = onPickPhotoButton,
            onStartLogout = onStartLogout,
            onLogoutButton = onLogoutButton,
            onInformationButton = onInformationButton,
            onBackButton = onBackButton
        )
    }
}