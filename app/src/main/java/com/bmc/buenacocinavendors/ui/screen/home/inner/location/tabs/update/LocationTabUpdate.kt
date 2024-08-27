package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.bmc.buenacocinavendors.core.LOCATION_TAB_UPDATE_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.LocationEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.LocationItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.LocationTabUpdateViewModel

@Composable
fun LocationTabUpdate(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: LocationTabUpdateViewModel = hiltViewModel(
        creationCallback = { factory: LocationTabUpdateViewModel.LocationTabUpdateViewModelFactory ->
            factory.create(storeId)
        }
    ),
    locations: LazyPagingItems<LocationDomain>,
    onSuccessfulUpdate: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LocationTabUpdateViewModel.ValidationEvent.Failure -> {
                    Log.e("LocationTabUpdate", "LocationTabUpdate: ${event.error}")
                }

                LocationTabUpdateViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Direccion actualizada con exito",
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

    LocationTabUpdateContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        locations = locations,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun LocationTabUpdateContent(
    windowSizeClass: WindowSizeClass,
    uiState: LocationTabUpdateUiState,
    scrollState: ScrollState,
    locations: LazyPagingItems<LocationDomain>,
    onIntent: (LocationTabUpdateIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "¿Que direccion actualizar?",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (locations.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.currentLocationUpdate != null) {
                    onIntent(LocationTabUpdateIntent.LocationUpdateChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(LOCATION_TAB_UPDATE_SHIMMER_ITEM_COUNT) {
                        LocationItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (locations.itemCount == 0) {
                    LocationEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = locations.itemCount,
                            key = locations.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val location = locations[index]
                            if (location != null) {
                                LocationTabUpdateItem(
                                    location = location,
                                    onClick = { loc ->
                                        onIntent(
                                            LocationTabUpdateIntent.LocationUpdateChanged(loc)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.currentLocationUpdateError != null) {
            Text(
                text = uiState.currentLocationUpdateError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Text(
            text = "Nombre de la direccion",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.name,
            onValueChange = { newName ->
                onIntent(LocationTabUpdateIntent.NameChanged(newName))
            },
            placeholder = {
                Text(text = "ej. Enfrente del Chedraui (modulo D)")
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
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Breve descripcion de la direccion",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            value = uiState.description,
            onValueChange = { newDescription ->
                onIntent(LocationTabUpdateIntent.DescriptionChanged(newDescription))
            },
            placeholder = {
                Text(text = "ej. En las mesas")
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
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Cambios",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.currentLocationUpdate != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.currentLocationUpdate.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                )
                Text(
                    text = uiState.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.currentLocationUpdate.description,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                )
                Text(
                    text = uiState.description,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
            }
        } else {
            Text(
                text = makeBulletedList(items = listOf("No se han aplicado cambios")),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                onIntent(LocationTabUpdateIntent.Submit)
            },
            enabled = !uiState.isWaitingForResult,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.CenterHorizontally)
                .size(200.dp, 50.dp)
        ) {
            if (uiState.isWaitingForResult) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(20.dp),
                )
            } else {
                Text(
                    text = "Actualizar",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
