package com.bmc.buenacocinavendors.ui.screen.home.inner.location

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.add.LocationTabAdd
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete.LocationTabDelete
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.update.LocationTabUpdate
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.visualizer.LocationTabVisualizerScreen
import com.bmc.buenacocinavendors.ui.viewmodel.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    viewModel: LocationViewModel = hiltViewModel(
        creationCallback = { factory: LocationViewModel.LocationViewModelFactory ->
            factory.create(storeId)
        }
    ),
    pagerState: PagerState = rememberPagerState(
        pageCount = { LocationTabDestination.entries.size }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onLocationSuccessfulCreation: () -> Unit,
    onLocationSuccessfulUpdate: () -> Unit,
    onLocationSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    val locations = viewModel.locations.collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LocationScreenContent(
        windowSizeClass = windowSizeClass,
        storeId = storeId,
        snackbarHostState = snackbarHostState,
        locations = locations,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        onLocationSuccessfulCreation = onLocationSuccessfulCreation,
        onLocationSuccessfulUpdate = onLocationSuccessfulUpdate,
        onLocationSuccessfulDelete = onLocationSuccessfulDelete,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreenContent(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    locations: LazyPagingItems<LocationDomain>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onLocationSuccessfulCreation: () -> Unit,
    onLocationSuccessfulUpdate: () -> Unit,
    onLocationSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Direcciones",
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
                LocationTabDestination.entries.fastForEachIndexed { i, tab ->
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
                when (LocationTabDestination.entries[pagerState.currentPage]) {
                    LocationTabDestination.ADD -> {
                        LocationTabAdd(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            onSuccessfulCreation = onLocationSuccessfulCreation
                        )
                    }

                    LocationTabDestination.UPDATE -> {
                        LocationTabUpdate(
                            windowSizeClass = windowSizeClass,
                            storeId = storeId,
                            snackbarHostState = snackbarHostState,
                            locations = locations,
                            onSuccessfulUpdate = onLocationSuccessfulUpdate
                        )
                    }

                    LocationTabDestination.DELETE -> {
                        LocationTabDelete(
                            windowSizeClass = windowSizeClass,
                            snackbarHostState = snackbarHostState,
                            locations = locations,
                            onSuccessfulDelete = onLocationSuccessfulDelete
                        )
                    }

                    LocationTabDestination.VISUALIZER -> {
                        LocationTabVisualizerScreen(
                            windowSizeClass = windowSizeClass,
                            locations = locations
                        )
                    }
                }
            }
        }
    }
}