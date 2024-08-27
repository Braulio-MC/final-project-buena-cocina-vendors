package com.bmc.buenacocinavendors.ui.screen.home.inner.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add.ProductTabAdd
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.delete.ProductTabDelete
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update.ProductTabUpdate
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.visualizer.ProductTabVisualizerScreen
import com.bmc.buenacocinavendors.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProductScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    storeName: String,
    viewModel: ProductViewModel = hiltViewModel(
        creationCallback = { factory: ProductViewModel.ProductViewModelFactory ->
            factory.create(storeId)
        }
    ),
    pagerState: PagerState = rememberPagerState(
        pageCount = { ProductTabDestination.entries.size }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onProductSuccessfulCreation: () -> Unit,
    onProductSuccessfulUpdate: () -> Unit,
    onProductSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    val products = viewModel.products.collectAsLazyPagingItems()
    val categories = viewModel.categories.collectAsLazyPagingItems()
    val generalCategories = viewModel.generalCategories.collectAsLazyPagingItems()
    val discounts = viewModel.discounts.collectAsLazyPagingItems()
    val defaultDiscounts = viewModel.defaultDiscount.collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    ProductScreenContent(
        windowSizeClass = windowSizeClass,
        storeId = storeId,
        storeName = storeName,
        snackbarHostState = snackbarHostState,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        products = products,
        categories = categories,
        generalCategories = generalCategories,
        discounts = discounts,
        defaultDiscounts = defaultDiscounts.value,
        onProductSuccessfulCreation = onProductSuccessfulCreation,
        onProductSuccessfulUpdate = onProductSuccessfulUpdate,
        onProductSuccessfulDelete = onProductSuccessfulDelete,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreenContent(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    storeName: String,
    snackbarHostState: SnackbarHostState,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    products: LazyPagingItems<ProductDomain>,
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    discounts: LazyPagingItems<DiscountDomain>,
    defaultDiscounts: List<DiscountDomain>?,
    onProductSuccessfulCreation: () -> Unit,
    onProductSuccessfulUpdate: () -> Unit,
    onProductSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Productos",
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
                .fillMaxSize()
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp
            ) {
                ProductTabDestination.entries.fastForEachIndexed { i, tab ->
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
                                    contentDescription = stringResource(id = tab.contentDescription)
                                )
                            } else {
                                Icon(
                                    imageVector = tab.unselectedIcon,
                                    contentDescription = stringResource(id = tab.contentDescription)
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
                when (ProductTabDestination.entries[pagerState.currentPage]) {
                    ProductTabDestination.ADD -> {
                        ProductTabAdd(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            storeName = storeName,
                            snackbarHostState = snackbarHostState,
                            categories = categories,
                            generalCategories = generalCategories,
                            discounts = discounts,
                            defaultDiscounts = defaultDiscounts,
                            onSuccessfulCreation = onProductSuccessfulCreation
                        )
                    }

                    ProductTabDestination.UPDATE -> {
                        ProductTabUpdate(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            storeName = storeName,
                            snackbarHostState = snackbarHostState,
                            products = products,
                            categories = categories,
                            generalCategories = generalCategories,
                            discounts = discounts,
                            defaultDiscounts = defaultDiscounts,
                            onSuccessfulUpdate = onProductSuccessfulUpdate
                        )
                    }

                    ProductTabDestination.DELETE -> {
                        ProductTabDelete(
                            windowSizeClass = windowSizeClass,
                            snackbarHostState = snackbarHostState,
                            products = products,
                            onSuccessfulDelete = onProductSuccessfulDelete
                        )
                    }

                    ProductTabDestination.VISUALIZER -> {
                        ProductTabVisualizerScreen(
                            windowSizeClass = windowSizeClass,
                            products = products,
                            onProductItemClick = { }
                        )
                    }
                }
            }
        }
    }
}