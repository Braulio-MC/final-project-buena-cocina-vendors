package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer

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
import com.bmc.buenacocinavendors.core.DISCOUNT_TAB_VISUALIZER_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

@Composable
fun DiscountTabVisualizerScreen(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState = rememberScrollState(),
    discounts: LazyPagingItems<DiscountDomain>,
    onDiscountItemClick: (String, String) -> Unit
) {
    DiscountTabVisualizerScreenContent(
        windowSizeClass = windowSizeClass,
        scrollState = scrollState,
        discounts = discounts,
        onDiscountItemClick = onDiscountItemClick
    )
}

@Composable
fun DiscountTabVisualizerScreenContent(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState,
    discounts: LazyPagingItems<DiscountDomain>,
    onDiscountItemClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (discounts.loadState.refresh) {
            is LoadState.Error -> {
                Log.e("Error", discounts.loadState.hasError.toString())
            }

            LoadState.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .heightIn(max = 1000.dp)
                ) {
                    items(DISCOUNT_TAB_VISUALIZER_SHIMMER_ITEM_COUNT) {
                        DiscountTabVisualizerItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (discounts.itemCount == 0) {
                    DiscountTabVisualizerEmpty()
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
                            count = discounts.itemCount,
                            key = discounts.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val discount = discounts[index]
                            if (discount != null) {
                                DiscountTabVisualizerItem(
                                    discount = discount,
                                    onClick = onDiscountItemClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}