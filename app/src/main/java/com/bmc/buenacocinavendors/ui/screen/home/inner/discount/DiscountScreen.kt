package com.bmc.buenacocinavendors.ui.screen.home.inner.discount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add.DiscountTabAdd
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.delete.DiscountTabDelete
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.update.DiscountTabUpdate
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.DiscountTabVisualizerScreen
import com.bmc.buenacocinavendors.ui.viewmodel.DiscountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DiscountScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    viewModel: DiscountViewModel = hiltViewModel(
        creationCallback = { factory: DiscountViewModel.DiscountViewModelFactory ->
            factory.create(storeId)
        }
    ),
    pagerState: PagerState = rememberPagerState(
        pageCount = { DiscountTabDestination.entries.size }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDiscountVisualizerItemClick: (String, String) -> Unit,
    onDiscountSuccessfulCreation: () -> Unit,
    onDiscountSuccessfulUpdate: () -> Unit,
    onDiscountSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    val discounts = viewModel.discounts.collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    DiscountScreenContent(
        windowSizeClass = windowSizeClass,
        storeId = storeId,
        snackbarHostState = snackbarHostState,
        discounts = discounts,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        onDiscountVisualizerItemClick = onDiscountVisualizerItemClick,
        onDiscountSuccessfulCreation = onDiscountSuccessfulCreation,
        onDiscountSuccessfulUpdate = onDiscountSuccessfulUpdate,
        onDiscountSuccessfulDelete = onDiscountSuccessfulDelete,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountScreenContent(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    discounts: LazyPagingItems<DiscountDomain>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onDiscountVisualizerItemClick: (String, String) -> Unit,
    onDiscountSuccessfulCreation: () -> Unit,
    onDiscountSuccessfulUpdate: () -> Unit,
    onDiscountSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Descuentos",
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
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp
            ) {
                DiscountTabDestination.entries.fastForEachIndexed { i, tab ->
                    Tab(
                        selected = i == pagerState.currentPage,
                        text = {
                            Text(
                                text = stringResource(id = tab.label)
                            )
                        },
                        icon = {
                            if (i == pagerState.currentPage) {
                                Icon(
                                    imageVector = tab.selectedIcon,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    imageVector = tab.unselectedIcon,
                                    contentDescription = null
                                )
                            }
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState
            ) {
                when (DiscountTabDestination.entries[pagerState.currentPage]) {
                    DiscountTabDestination.ADD -> {
                        DiscountTabAdd(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            onSuccessfulCreation = onDiscountSuccessfulCreation
                        )
                    }

                    DiscountTabDestination.UPDATE -> {
                        DiscountTabUpdate(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            discounts = discounts,
                            onSuccessfulUpdate = onDiscountSuccessfulUpdate,
                        )
                    }

                    DiscountTabDestination.DELETE -> {
                        DiscountTabDelete(
                            windowSizeClass = windowSizeClass,
                            snackbarHostState = snackbarHostState,
                            discounts = discounts,
                            onSuccessfulDelete = onDiscountSuccessfulDelete,
                        )
                    }

                    DiscountTabDestination.VISUALIZER -> {
                        DiscountTabVisualizerScreen(
                            windowSizeClass = windowSizeClass,
                            discounts = discounts,
                            onDiscountItemClick = onDiscountVisualizerItemClick
                        )
                    }
                }
            }
        }
    }
}