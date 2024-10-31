package com.bmc.buenacocinavendors.ui.screen.home.inner.store.visualizer

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.screen.common.NoInternetScreen
import com.bmc.buenacocinavendors.ui.viewmodel.StoreVisualizerViewModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreVisualizerScreen(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    viewModel: StoreVisualizerViewModel = hiltViewModel(
        creationCallback = { factory: StoreVisualizerViewModel.StoreVisualizerViewModelFactory ->
            factory.create(storeId)
        }
    ),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onBackButton: () -> Unit
) {
    val uiState = viewModel.store
        .map { store -> StoreVisualizerUiState(store = store) }
        .collectAsStateWithLifecycle(initialValue = StoreVisualizerUiState(isLoading = true))
    val products = viewModel.products.collectAsLazyPagingItems()
    val netState = viewModel.netState.collectAsStateWithLifecycle()

    StoreVisualizerScreenContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        products = products,
        netState = netState.value,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreVisualizerScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: StoreVisualizerUiState,
    products: LazyPagingItems<ProductDomain>,
    netState: NetworkStatus,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackButton: () -> Unit
) {
    val storeName = if (uiState.store != null) uiState.store.name else ""

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = storeName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
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
        if (uiState.isLoading) {
            StoreVisualizerShimmer(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )
        } else {
            if (uiState.store != null) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.store.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Text(
                        text = uiState.store.name,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .size(200.dp, 70.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxSize()
                                    .weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "N/A",
                                    fontSize = 25.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    tint = colorResource(id = R.color.rating_star_filled),
                                    modifier = Modifier
                                        .size(35.dp)
                                        .weight(1f)
                                )
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(55.dp)
                                    .background(Color.Gray)
                            )
                            IconButton(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                enabled = false,
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .size(35.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Productos de ${uiState.store.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    when (products.loadState.refresh) {
                        is LoadState.Error -> {

                        }

                        LoadState.Loading -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .heightIn(max = 1000.dp)
                            ) {
                                items(10) {
                                    StoreVisualizerItemShimmer()
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(count = 2),
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
                                    }) { index ->
                                    val product = products[index]
                                    if (product != null) {
                                        StoreVisualizerItem(
                                            product = product
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
//                if (netState == NetworkStatus.Unavailable || netState == NetworkStatus.Lost) {
//                    NoInternetScreen(
//                        paddingValues = PaddingValues()
//                    )
//                }
            }
        }
    }
}