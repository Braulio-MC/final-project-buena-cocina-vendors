package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete

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
import com.bmc.buenacocinavendors.core.PRODUCT_TAB_DELETE_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.ProductEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.ProductItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.ProductTabDeleteViewModel

@Composable
fun ProductTabDelete(
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: ProductTabDeleteViewModel = hiltViewModel(),
    products: LazyPagingItems<ProductDomain>,
    onSuccessfulDelete: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is ProductTabDeleteViewModel.ValidationEvent.Failure -> {
                    Log.e("ProductTabDelete", "Error: ${event.error})")
                }

                ProductTabDeleteViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Product eliminado",
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

    ProductTabDeleteContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        products = products,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun ProductTabDeleteContent(
    windowSizeClass: WindowSizeClass,
    uiState: ProductTabDeleteUiState,
    scrollState: ScrollState,
    products: LazyPagingItems<ProductDomain>,
    onIntent: (ProductTabDeleteIntent) -> Unit
) {
    val restrictions = listOf(
        stringResource(id = R.string.product_screen_tab_delete_restriction)
    )
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "¿Que producto eliminar?",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (products.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.productDelete != null) {
                    onIntent(ProductTabDeleteIntent.ProductDeleteChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(PRODUCT_TAB_DELETE_SHIMMER_ITEM_COUNT) {
                        ProductItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (products.itemCount == 0) {
                    ProductEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = products.itemCount,
                            key = products.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val product = products[index]
                            if (product != null) {
                                ProductTabDeleteItem(
                                    product = product,
                                    onClick = {
                                        onIntent(ProductTabDeleteIntent.ProductDeleteChanged(it))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.productDeleteError != null) {
            Text(
                text = uiState.productDeleteError.asString(),
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
        if (uiState.productDelete != null) {
            Text(
                text = "${uiState.productDelete.name} se intentara eliminar",
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
                onIntent(ProductTabDeleteIntent.Submit)
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