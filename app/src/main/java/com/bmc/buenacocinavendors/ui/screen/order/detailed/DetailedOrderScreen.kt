package com.bmc.buenacocinavendors.ui.screen.order.detailed

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.domain.model.OrderDomain
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
    val resultState = viewModel.resultState.collectAsStateWithLifecycle()
    val netState = viewModel.netState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var showOrderStatusBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

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
        resultState = resultState.value,
        netState = netState.value,
        snackbarHostState = snackbarHostState,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        onIntent = viewModel::onIntent,
        onOrderStatusClick = { showOrderStatusBottomSheet = true },
        onOrderRatedButton = { onOrderRatedButton(orderId, userId, storeId) },
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedOrderScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: DetailedOrderUiState,
    resultState: DetailedOrderUiResultState,
    netState: NetworkStatus,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (DetailedOrderIntent) -> Unit,
    onOrderStatusClick: () -> Unit,
    onOrderRatedButton: () -> Unit,
    onBackButton: () -> Unit
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {

    } else {
        DetailedOrderScreenCompactMedium(
            uiState = uiState,
            resultState = resultState,
            snackbarHostState = snackbarHostState,
            netState = netState,
            scrollState = scrollState,
            scrollBehavior = scrollBehavior,
            onIntent = onIntent,
            onOrderStatusClick = onOrderStatusClick,
            onOrderRatedButton = onOrderRatedButton,
            onBackButton = onBackButton
        )
    }
}