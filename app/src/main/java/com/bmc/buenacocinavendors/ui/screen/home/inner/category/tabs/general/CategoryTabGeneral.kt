package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.general

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.bmc.buenacocinavendors.core.CATEGORY_TAB_GENERAL_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.domain.model.CategoryDomain

@Composable
fun CategoryTabGeneralScreen(
    scrollState: ScrollState = rememberScrollState(),
    categories: LazyPagingItems<CategoryDomain>,
    onCategoryItemClick: (String) -> Unit
) {
    CategoryTabGeneralScreenContent(
        scrollState = scrollState,
        categories = categories,
        onItemClick = onCategoryItemClick
    )
}

@Composable
fun CategoryTabGeneralScreenContent(
    scrollState: ScrollState,
    categories: LazyPagingItems<CategoryDomain>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        when (categories.loadState.refresh) {
            is LoadState.Error -> {
                Log.d("CategoryTabVisualizerScreen", categories.loadState.hasError.toString())
            }

            LoadState.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .heightIn(max = 1000.dp)
                ) {
                    items(CATEGORY_TAB_GENERAL_SHIMMER_ITEM_COUNT) {
                        CategoryTabGeneralItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (categories.itemCount == 0) {
                    CategoryTabGeneralEmpty()
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
                            count = categories.itemCount,
                            key = categories.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val category = categories[index]
                            if (category != null) {
                                CategoryTabGeneralItem(
                                    category = category,
                                    onClick = onItemClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}