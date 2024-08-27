package com.bmc.buenacocinavendors.ui.screen.store

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationScreenExpanded(
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
    val currentContext = LocalContext.current

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.store_registration_screen_title),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButton() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onInformationButton() }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            onLogoutButtonChanged(false)
                            onStartLogout(currentContext, {
                                onLogoutButtonChanged(true)
                                onLogoutButton(false)
                            }, {
                                onLogoutButton(true)
                            })
                        },
                        enabled = isLogoutButtonEnabled
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
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
    ) {
        Row(
            modifier = Modifier
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.store_registration_screen_image_label),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .border(3.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(currentContext)
                            .data(uiState.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(230.dp)
                    )
                }
                Button(
                    onClick = { onPickPhotoButton() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(width = 270.dp, height = 50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.store_registration_screen_pick_photo_button_text),
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (uiState.imageError != null) {
                    Text(
                        text = uiState.imageError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.store_registration_screen_select_store_name_label),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            value = uiState.name,
                            onValueChange = { newName ->
                                onIntent(StoreRegistrationFormIntent.NameChanged(newName))
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.store_registration_screen_store_name_placeholder),
                                    fontSize = 19.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            singleLine = true,
                            isError = uiState.nameError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        if (uiState.nameError != null) {
                            Text(
                                text = uiState.nameError.asString(),
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.store_registration_screen_select_store_email_label),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            value = uiState.email,
                            onValueChange = { newEmail ->
                                onIntent(StoreRegistrationFormIntent.EmailChanged(newEmail))
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.store_registration_screen_store_email_placeholder),
                                    fontSize = 19.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            singleLine = true,
                            isError = uiState.emailError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        if (uiState.emailError != null) {
                            Text(
                                text = uiState.emailError.asString(),
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.store_registration_screen_select_store_phone_label),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            value = uiState.phoneNumber,
                            onValueChange = { newPhoneNumber ->
                                onIntent(StoreRegistrationFormIntent.PhoneChanged(newPhoneNumber))
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.store_registration_screen_store_phone_placeholder),
                                    fontSize = 19.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            singleLine = true,
                            isError = uiState.phoneNumberError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        if (uiState.phoneNumberError != null) {
                            Text(
                                text = uiState.phoneNumberError.asString(),
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1.4f)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.store_registration_screen_select_store_desc_label),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            value = uiState.description,
                            onValueChange = { newDescription ->
                                onIntent(
                                    StoreRegistrationFormIntent.DescriptionChanged(
                                        newDescription
                                    )
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.store_registration_screen_store_desc_placeholder),
                                    fontSize = 19.sp,
                                )
                            },
                            isError = uiState.descriptionError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        if (uiState.descriptionError != null) {
                            Text(
                                text = uiState.descriptionError.asString(),
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        onIntent(StoreRegistrationFormIntent.Submit)
                    },
                    enabled = !uiState.isWaitingForResult,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .size(width = 250.dp, height = 60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (uiState.isWaitingForResult) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(25.dp),
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.store_registration_screen_submit_button_text),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}