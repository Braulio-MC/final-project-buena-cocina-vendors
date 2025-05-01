package com.bmc.buenacocinavendors.ui.screen.store

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationScreenCompactMedium(
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
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.store_registration_screen_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButton() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onInformationButton() }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
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
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.store_registration_screen_image_label),
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
            }
            Button(
                onClick = { onPickPhotoButton() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.store_registration_screen_pick_photo_button_text),
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
                text = stringResource(id = R.string.store_registration_screen_select_store_name_label),
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
                    onIntent(StoreRegistrationFormIntent.NameChanged(newName))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.store_registration_screen_store_name_placeholder))
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
                text = stringResource(id = R.string.store_registration_screen_select_store_desc_label),
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
                    onIntent(StoreRegistrationFormIntent.DescriptionChanged(newDescription))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.store_registration_screen_store_desc_placeholder))
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
                text = stringResource(id = R.string.store_registration_screen_select_store_email_label),
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
                    onIntent(StoreRegistrationFormIntent.EmailChanged(newEmail))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.store_registration_screen_store_email_placeholder))
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
                text = stringResource(id = R.string.store_registration_screen_select_store_phone_label),
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
                    onIntent(StoreRegistrationFormIntent.PhoneChanged(newPhoneNumber))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.store_registration_screen_store_phone_placeholder))
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
            Text(
                text = "Hora de apertura (formato HH:mm)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.startTime,
                onValueChange = { newStartTime ->
                    onIntent(StoreRegistrationFormIntent.StartTimeChanged(newStartTime))
                },
                placeholder = { Text(text = "Ejemplo: 09:00") },
                singleLine = true,
                isError = uiState.startTimeError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Hora de cierre (formato HH:mm)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.endTime,
                onValueChange = { newEndTime ->
                    onIntent(StoreRegistrationFormIntent.EndTimeChanged(newEndTime))
                },
                placeholder = { Text(text = "Ejemplo: 23:00") },
                singleLine = true,
                isError = uiState.endTimeError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = {
                    onIntent(StoreRegistrationFormIntent.Submit)
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
                        text = stringResource(id = R.string.store_registration_screen_submit_button_text),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}