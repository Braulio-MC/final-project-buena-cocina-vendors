package com.bmc.buenacocinavendors.ui.screen.order.detailed

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.domain.LocationPermissionTextProvider
import com.bmc.buenacocinavendors.domain.getActivity
import com.bmc.buenacocinavendors.domain.hasLocationPermissionFlow
import com.bmc.buenacocinavendors.domain.mapper.asLatLng
import com.bmc.buenacocinavendors.ui.openAppSettings
import com.bmc.buenacocinavendors.ui.screen.common.LocationPermissionDialog
import com.bmc.buenacocinavendors.ui.viewmodel.DetailedOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedOrderScreen(
    windowSizeClass: WindowSizeClass,
    orderId: String,
    userId: String,
    storeId: String,
    viewModel: DetailedOrderViewModel = hiltViewModel(
        creationCallback = { factory: DetailedOrderViewModel.DetailedOrderViewModelFactory ->
            factory.create(orderId = orderId)
        }
    ),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollState: ScrollState = rememberScrollState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    orderStatusSheetState: SheetState = rememberModalBottomSheetState(),
    onChannelCreatedSuccessful: (String) -> Unit,
    onOrderRatedButton: (String, String, String) -> Unit,
    onBackButton: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val locationPermissionQueue by viewModel.visiblePermissionDialogQueue.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val netState = viewModel.netState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var showOrderStatusBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var showOrderLocationDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                isGranted = isGranted
            )
        }
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val isForeground = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is DetailedOrderViewModel.ValidateEvent.UpdateStatusFailure -> {
                    Log.e("DetailedOrderScreen", "Error ${event.error}")
                }

                DetailedOrderViewModel.ValidateEvent.UpdateStatusSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "Estado actualizado",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }

                is DetailedOrderViewModel.ValidateEvent.CreateChannelFailure -> {
                    snackbarHostState.showSnackbar(
                        message = "No se pudo crear el canal de mensajes",
                        withDismissAction = true
                    )
                }

                is DetailedOrderViewModel.ValidateEvent.CreateChannelSuccess -> {
                    onChannelCreatedSuccessful(event.channelId)
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isForeground.value = event == Lifecycle.Event.ON_RESUME
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(isForeground.value, showOrderLocationDialog) {
        if (isForeground.value && showOrderLocationDialog) {
            currentContext.hasLocationPermissionFlow().collect { hasLocationPermission ->
                if (hasLocationPermission && showOrderLocationDialog) {
                    viewModel.startLocationUpdates()
                } else {
                    viewModel.stopLocationUpdates()
                }
            }
        } else {
            viewModel.stopLocationUpdates()
        }
    }

    locationPermissionQueue
        .reversed()
        .forEach { permission ->
            LocationPermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> LocationPermissionTextProvider()
                    else -> return@forEach
                },
                isPermanentlyDeclined = if (currentContext.getActivity() != null) {
                    !shouldShowRequestPermissionRationale(
                        currentContext.getActivity()!!,
                        permission
                    )
                } else false,
                onDismiss = viewModel::dismissPermissionDialog,
                onOkClick = {
                    viewModel.dismissPermissionDialog()
                    locationPermissionResultLauncher.launch(permission)
                },
                onGoToAppSettingsClick = if (currentContext.getActivity() != null) {
                    { currentContext.getActivity()!!.openAppSettings() }
                } else {
                    {}
                }
            )
        }

    if (uiState.value.cuceiCenterOnMap != null && uiState.value.cuceiAreaBoundsOnMap != null) {
        DetailedOrderLocationDialog(
            isDialogOpen = showOrderLocationDialog,
            isLoadingUserLocation = uiState.value.isLoadingUserLocation,
            cuceiCenter = uiState.value.cuceiCenterOnMap!!,
            cuceiBounds = uiState.value.cuceiAreaBoundsOnMap!!,
            orderLocation = uiState.value.order?.deliveryLocation?.asLatLng(),
            currentUserLocation = uiState.value.userLocation,
            onDismiss = { showOrderLocationDialog = false }
        )
    }

    if (showOrderStatusBottomSheet) {
        DetailedOrderStatusBottomSheet(
            sheetState = orderStatusSheetState,
            onDetailedOrderStatusBottomSheetItemClick = { status ->
                viewModel.onIntent(DetailedOrderIntent.ChangeStatus(status))
                showOrderStatusBottomSheet = false
            },
            onDismissRequest = { showOrderStatusBottomSheet = false }
        )
    }

    DetailedOrderScreenContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        netState = netState.value,
        snackbarHostState = snackbarHostState,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        onIntent = viewModel::onIntent,
        onOrderStatusClick = { showOrderStatusBottomSheet = true },
        onOrderRatedButton = { onOrderRatedButton(orderId, userId, storeId) },
        onShowLocationOnMapButton = {
            showOrderLocationDialog = true
            locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        },
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedOrderScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: DetailedOrderUiState,
    netState: NetworkStatus,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (DetailedOrderIntent) -> Unit,
    onOrderStatusClick: () -> Unit,
    onOrderRatedButton: () -> Unit,
    onShowLocationOnMapButton: () -> Unit,
    onBackButton: () -> Unit
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {

    } else {
        DetailedOrderScreenCompactMedium(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            netState = netState,
            scrollState = scrollState,
            scrollBehavior = scrollBehavior,
            onIntent = onIntent,
            onOrderStatusClick = onOrderStatusClick,
            onOrderRatedButton = onOrderRatedButton,
            onShowLocationOnMapButton = onShowLocationOnMapButton,
            onBackButton = onBackButton
        )
    }
}