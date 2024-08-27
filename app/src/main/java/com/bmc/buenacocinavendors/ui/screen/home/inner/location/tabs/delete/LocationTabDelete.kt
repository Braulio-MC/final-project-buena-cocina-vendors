package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.API_SERVER_CACHE_TIME_IN_MIN
import com.bmc.buenacocinavendors.core.LOCATION_TAB_DELETE_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.LocationEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.LocationItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.LocationTabDeleteViewModel

@Composable
fun LocationTabDelete(
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: LocationTabDeleteViewModel = hiltViewModel(),
    locations: LazyPagingItems<LocationDomain>,
    onSuccessfulDelete: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LocationTabDeleteViewModel.ValidationEvent.Failure -> {
                    Log.e("LocationTabDelete", "Error: ${event.error})")
                }

                LocationTabDeleteViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Direccion eliminada con éxito",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulDelete()
                    }
                }
            }
        }
    }

    LocationTabDeleteContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        locations = locations,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun LocationTabDeleteContent(
    windowSizeClass: WindowSizeClass,
    uiState: LocationTabDeleteUiState,
    scrollState: ScrollState,
    locations: LazyPagingItems<LocationDomain>,
    onIntent: (LocationTabDeleteIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val restrictions = listOf(
            stringResource(id = R.string.location_screen_tab_delete_restriction)
        )
        Text(
            text = "¿Que direccion eliminar?",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (locations.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.locationDelete != null) {
                    onIntent(LocationTabDeleteIntent.LocationDeleteChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(LOCATION_TAB_DELETE_SHIMMER_ITEM_COUNT) {
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
                                LocationTabDeleteItem(
                                    location = location,
                                    onClick = { loc ->
                                        onIntent(
                                            LocationTabDeleteIntent.LocationDeleteChanged(loc)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.locationDeleteError != null) {
            Text(
                text = uiState.locationDeleteError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Text(
            text = "Restricciones",
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        )
        Text(
            text = makeBulletedList(items = restrictions),
            fontSize = 17.sp,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = "Cambios",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.locationDelete != null) {
            Text(
                text = "${uiState.locationDelete.name} se intentara eliminar",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
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
                onIntent(LocationTabDeleteIntent.Submit)
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
                    text = "Eliminar",
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