package com.bmc.buenacocinavendors.ui.screen.home.inner.category

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
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add.CategoryTabAdd
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.delete.CategoryTabDelete
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.general.CategoryTabGeneralScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update.CategoryTabUpdate
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.visualizer.CategoryTabVisualizerScreen
import com.bmc.buenacocinavendors.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    viewModel: CategoryViewModel = hiltViewModel(
        creationCallback = { factory: CategoryViewModel.CategoryViewModelFactory ->
            factory.create(storeId)
        }
    ),
    pagerState: PagerState = rememberPagerState(
        pageCount = { CategoryTabDestination.entries.size }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onCategoryVisualizerItemClick: (String, String) -> Unit,
    onCategoryGeneralItemClick: (String) -> Unit,
    onCategorySuccessfulCreation: () -> Unit,
    onCategorySuccessfulUpdate: () -> Unit,
    onCategorySuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    val categories = viewModel.categories.collectAsLazyPagingItems()
    val generalCategories = viewModel.generalCategories.collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    CategoryScreenContent(
        windowSizeClass = windowSizeClass,
        storeId = storeId,
        snackbarHostState = snackbarHostState,
        categories = categories,
        generalCategories = generalCategories,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        onCategoryVisualizerItemClick = onCategoryVisualizerItemClick,
        onCategoryGeneralItemClick = onCategoryGeneralItemClick,
        onCategorySuccessfulCreation = onCategorySuccessfulCreation,
        onCategorySuccessfulUpdate = onCategorySuccessfulUpdate,
        onCategorySuccessfulDelete = onCategorySuccessfulDelete,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onCategoryVisualizerItemClick: (String, String) -> Unit,
    onCategoryGeneralItemClick: (String) -> Unit,
    onCategorySuccessfulCreation: () -> Unit,
    onCategorySuccessfulUpdate: () -> Unit,
    onCategorySuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorias",
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
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp
            ) {
                CategoryTabDestination.entries.fastForEachIndexed { i, tab ->
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
                state = pagerState,
            ) {
                when (CategoryTabDestination.entries[pagerState.currentPage]) {
                    CategoryTabDestination.ADD -> {
                        CategoryTabAdd(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            categories = categories,
                            generalCategories = generalCategories,
                            onSuccessfulCreation = onCategorySuccessfulCreation
                        )
                    }

                    CategoryTabDestination.UPDATE -> {
                        CategoryTabUpdate(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            categories = categories,
                            generalCategories = generalCategories,
                            onSuccessfulUpdate = onCategorySuccessfulUpdate
                        )
                    }

                    CategoryTabDestination.DELETE -> {
                        CategoryTabDelete(
                            windowSizeClass = windowSizeClass,
                            snackbarHostState = snackbarHostState,
                            categories = categories,
                            onSuccessfulDelete = onCategorySuccessfulDelete
                        )
                    }

                    CategoryTabDestination.VISUALIZER -> {
                        CategoryTabVisualizerScreen(
                            categories = categories,
                            onCategoryItemClick = onCategoryVisualizerItemClick
                        )
                    }

                    CategoryTabDestination.VISUALIZER_GENERAL -> {
                        CategoryTabGeneralScreen(
                            categories = generalCategories,
                            onCategoryItemClick = onCategoryGeneralItemClick
                        )
                    }
                }
            }
        }
    }
}