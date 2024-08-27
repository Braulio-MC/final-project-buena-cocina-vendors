package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.visualizer

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
import com.bmc.buenacocinavendors.core.LOCATION_TAB_VISUALIZER_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.domain.model.LocationDomain

@Composable
fun LocationTabVisualizerScreen(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState = rememberScrollState(),
    locations: LazyPagingItems<LocationDomain>
) {
    LocationTabVisualizerScreenContent(
        windowSizeClass = windowSizeClass,
        scrollState = scrollState,
        locations = locations
    )
}

@Composable
fun LocationTabVisualizerScreenContent(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState,
    locations: LazyPagingItems<LocationDomain>,
) {

    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (locations.loadState.refresh) {
            is LoadState.Error -> {
                Log.d("LocationTabVisualizerScreen", locations.loadState.hasError.toString())
            }

            LoadState.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .heightIn(max = 1000.dp)
                ) {
                    items(LOCATION_TAB_VISUALIZER_SHIMMER_ITEM_COUNT) {
                        LocationTabVisualizerItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (locations.itemCount == 0) {
                    LocationTabVisualizerEmpty()
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
                            count = locations.itemCount,
                            key = locations.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val location = locations[index]
                            if (location != null) {
                                LocationTabVisualizerItem(
                                    location = location
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}