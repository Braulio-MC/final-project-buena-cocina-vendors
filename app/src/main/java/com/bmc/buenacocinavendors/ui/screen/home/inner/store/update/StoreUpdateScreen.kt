package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.ui.viewmodel.StoreUpdateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreUpdateScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    viewModel: StoreUpdateViewModel = hiltViewModel(creationCallback = { factory: StoreUpdateViewModel.StoreUpdateViewModelFactory ->
        factory.create(storeId)
    }),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onSuccessfulUpdate: () -> Unit,
    onBackButton: () -> Unit,
    onInformationButton: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val netState = viewModel.netState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onIntent(StoreUpdateIntent.ImageChanged(uri))
        }
    )
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is StoreUpdateViewModel.ValidationEvent.Failure -> {
                    Log.e("StoreUpdateScreen", "Error: ${event.error}")
                }

                is StoreUpdateViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Tienda actualizada con exito",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulUpdate()
                    }
                }
            }
        }
    }

    StoreUpdateScreenContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        netState = netState.value,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        onPickPhotoButton = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onBackButton = onBackButton,
        onInformationButton = onInformationButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreUpdateScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: StoreUpdateUiState,
    netState: NetworkStatus,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: SnackbarHostState,
    onIntent: (StoreUpdateIntent) -> Unit,
    onPickPhotoButton: () -> Unit,
    onBackButton: () -> Unit,
    onInformationButton: () -> Unit
) {
    val storeNamePlaceholder =
        stringResource(id = R.string.store_update_screen_store_name_placeholder)
    val storeDescPlaceholder =
        stringResource(id = R.string.store_update_screen_store_desc_placeholder)
    val storeEmailPlaceholder =
        stringResource(id = R.string.store_update_screen_store_email_placeholder)
    val storePhonePlaceholder =
        stringResource(id = R.string.store_update_screen_store_phone_placeholder)
    val currentContext = LocalContext.current

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.store_update_screen_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButton() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onInformationButton() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    ) { paddingValues ->
        if (uiState.isLoadingResources) {
            StoreUpdateShimmer(paddingValues = paddingValues)
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = stringResource(id = R.string.store_update_screen_select_image_label),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally)
                        .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.image != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(currentContext)
                                .data(uiState.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .size(135.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.image_not_loaded_error),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .size(90.dp)
                        )
                    }
                }
                Button(
                    onClick = { onPickPhotoButton() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.store_update_screen_pick_photo_button_text),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (uiState.imageError != null) {
                    Text(
                        text = uiState.imageError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(id = R.string.store_update_screen_select_store_name_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.name,
                    onValueChange = { newName ->
                        onIntent(StoreUpdateIntent.NameChanged(newName))
                    },
                    placeholder = {
                        Text(text = storeNamePlaceholder)
                    },
                    singleLine = true,
                    isError = uiState.nameError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                if (uiState.nameError != null) {
                    Text(
                        text = uiState.nameError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = stringResource(id = R.string.store_update_screen_select_store_desc_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    value = uiState.description,
                    onValueChange = { newDescription ->
                        onIntent(StoreUpdateIntent.DescriptionChanged(newDescription))
                    },
                    placeholder = {
                        Text(text = storeDescPlaceholder)
                    },
                    isError = uiState.descriptionError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                if (uiState.descriptionError != null) {
                    Text(
                        text = uiState.descriptionError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = stringResource(id = R.string.store_update_screen_select_store_email_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = { newEmail ->
                        onIntent(StoreUpdateIntent.EmailChanged(newEmail))
                    },
                    placeholder = {
                        Text(text = storeEmailPlaceholder)
                    },
                    singleLine = true,
                    isError = uiState.emailError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (uiState.emailError != null) {
                    Text(
                        text = uiState.emailError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = stringResource(id = R.string.store_update_screen_select_store_phone_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.phoneNumber,
                    onValueChange = { newPhoneNumber ->
                        onIntent(StoreUpdateIntent.PhoneChanged(newPhoneNumber))
                    },
                    placeholder = {
                        Text(text = storePhonePlaceholder)
                    },
                    singleLine = true,
                    isError = uiState.phoneNumberError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (uiState.phoneNumberError != null) {
                    Text(
                        text = uiState.phoneNumberError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(2.dp))
                StoreUpdateTimeSection(
                    startTime = uiState.startTime,
                    endTime = uiState.endTime,
                    onOpeningTimeChange = { hour, minute ->
                        onIntent(StoreUpdateIntent.StartTimeChanged(hour, minute))
                    },
                    onClosingTimeChange = { hour, minute ->
                        onIntent(StoreUpdateIntent.EndTimeChanged(hour, minute))
                    }
                )
                Button(
                    onClick = {
                        onIntent(StoreUpdateIntent.Submit)
                    },
                    enabled = !uiState.isWaitingForResult,
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (uiState.isWaitingForResult) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(20.dp),
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.store_update_screen_submit_button_text),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}