package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete

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
import com.bmc.buenacocinavendors.core.DISCOUNT_TAB_DELETE_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.DiscountEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.DiscountItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.DiscountTabDeleteViewModel

@Composable
fun DiscountTabDelete(
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: DiscountTabDeleteViewModel = hiltViewModel(),
    discounts: LazyPagingItems<DiscountDomain>,
    onSuccessfulDelete: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is DiscountTabDeleteViewModel.ValidationEvent.Failure -> {
                    Log.e("DiscountTabDelete", "Error: ${event.error}")
                }

                DiscountTabDeleteViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Descuento eliminado correctamente",
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

    DiscountTabDeleteContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        discounts = discounts,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun DiscountTabDeleteContent(
    windowSizeClass: WindowSizeClass,
    uiState: DiscountTabDeleteUiState,
    scrollState: ScrollState,
    discounts: LazyPagingItems<DiscountDomain>,
    onIntent: (DiscountTabDeleteIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "¿Que descuento eliminar?",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (discounts.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.discountDelete != null) {
                    onIntent(DiscountTabDeleteIntent.DiscountDeleteChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(DISCOUNT_TAB_DELETE_SHIMMER_ITEM_COUNT) {
                        DiscountItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (discounts.itemCount == 0) {
                    DiscountEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = discounts.itemCount,
                            key = discounts.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val discount = discounts[index]
                            if (discount != null) {
                                DiscountTabDeleteItem(
                                    discount = discount,
                                    onClick = { dis ->
                                        onIntent(
                                            DiscountTabDeleteIntent.DiscountDeleteChanged(dis)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.discountDeleteError != null) {
            Text(
                text = uiState.discountDeleteError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Text(
            text = "Cambios",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.discountDelete != null) {
            Text(
                text = "${uiState.discountDelete.name} se intentara eliminar",
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
                onIntent(DiscountTabDeleteIntent.Submit)
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