package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.visualizer

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.bmc.buenacocinavendors.core.PRODUCT_TAB_VISUALIZER_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.domain.model.ProductDomain

@Composable
fun ProductTabVisualizerScreen(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState = rememberScrollState(),
    products: LazyPagingItems<ProductDomain>,
    onProductItemClick: (String) -> Unit
) {
    ProductTabVisualizerScreenContent(
        windowSizeClass = windowSizeClass,
        scrollState = scrollState,
        products = products,
        onProductItemClick = onProductItemClick
    )
}

@Composable
fun ProductTabVisualizerScreenContent(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState,
    products: LazyPagingItems<ProductDomain>,
    onProductItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (products.loadState.refresh) {
            is LoadState.Error -> {
                Log.e(
                    "ProductTabVisualizerScreenContent",
                    (products.loadState.refresh as LoadState.Error).error.toString()
                )
            }

            LoadState.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .heightIn(max = 1000.dp)
                ) {
                    items(PRODUCT_TAB_VISUALIZER_SHIMMER_ITEM_COUNT) {
                        ProductTabVisualizerItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (products.itemCount == 0) {
                    ProductTabVisualizerEmpty()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(max = 1000.dp)
                            .nestedScroll(connection = object : NestedScrollConnection {
                                override fun onPreScroll(
                                    available: Offset,
                                    source: NestedScrollSource
                                ): Offset {
                                    if (scrollState.canScrollForward && available.y < 0) {
                                        val consumed =
                                            scrollState.dispatchRawDelta(-available.y)
                                        return Offset(x = 0f, y = -consumed)
                                    }
                                    return Offset.Zero
                                }
                            })
                    ) {
                        items(
                            count = products.itemCount,
                            key = products.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val product = products[index]
                            if (product != null) {
                                ProductTabVisualizerItem(
                                    product = product,
                                    onClick = onProductItemClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}