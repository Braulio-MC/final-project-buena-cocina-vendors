package com.bmc.buenacocinavendors.ui.screen.order

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bmc.buenacocinavendors.core.ORDER_SCREEN_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.ui.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: OrderViewModel = hiltViewModel(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    onOrderItemClick: (String) -> Unit,
    onBackButton: () -> Unit
) {
    val orders = viewModel.orders().collectAsLazyPagingItems()

    OrderScreenContent(
        windowSizeClass = windowSizeClass,
        orders = orders,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        onBackButton = onBackButton,
        onOrderItemClick = onOrderItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreenContent(
    windowSizeClass: WindowSizeClass,
    orders: LazyPagingItems<OrderDomain>,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    onOrderItemClick: (String) -> Unit,
    onBackButton: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pedidos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButton() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (orders.loadState.refresh) {
                is LoadState.Error -> {

                }

                LoadState.Loading -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(max = 1000.dp)
                    ) {
                        items(ORDER_SCREEN_SHIMMER_ITEM_COUNT) {
                            OrderItemShimmer()
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    if (orders.itemCount == 0) {
                        OrderEmpty(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        )
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
                                count = orders.itemCount,
                                key = orders.itemKey { item ->
                                    item.id
                                }
                            ) { index ->
                                val order = orders[index]
                                if (order != null) {
                                    OrderItem(
                                        order = order,
                                        onClick = onOrderItemClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}