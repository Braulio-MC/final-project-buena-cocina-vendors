package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.detailed

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.DISCOUNT_TAB_VISUALIZER_ITEM_DETAILED_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.core.TimeUtils
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.viewmodel.DiscountTabVisualizerItemDetailedViewModel
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountTabVisualizerItemDetailed(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    discountId: String,
    viewModel: DiscountTabVisualizerItemDetailedViewModel = hiltViewModel(
        creationCallback = { factory: DiscountTabVisualizerItemDetailedViewModel.DiscountTabVisualizerItemDetailedViewModelFactory ->
            factory.create(discountId, storeId)
        }
    ),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onBackButton: () -> Unit,
) {
    val uiState = viewModel.discount
        .map { discount -> DiscountTabVisualizerItemDetailedUiState(discount = discount) }
        .collectAsStateWithLifecycle(
            initialValue = DiscountTabVisualizerItemDetailedUiState(
                isLoading = true
            )
        )
    val products = viewModel.products.collectAsLazyPagingItems()

    DiscountTabVisualizerItemDetailedContent(
        windowSizeClass = windowSizeClass,
        scrollState = scrollState,
        scrollBehavior = scrollBehavior,
        uiState = uiState.value,
        products = products,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountTabVisualizerItemDetailedContent(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: DiscountTabVisualizerItemDetailedUiState,
    products: LazyPagingItems<ProductDomain>,
    onBackButton: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalles del descuento",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButton() }
                    ) {
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
            DiscountTabVisualizerItemDetailedShimmer(paddingValues)
        } else {
            if (uiState.discount != null) {
                val percentage = uiState.discount.percentage.toPlainString()
                val remainingStartEndText =
                    if (uiState.discount.startDate != null && uiState.discount.endDate != null) {
                        DateUtils.getRemainingPeriodString(
                            uiState.discount.startDate,
                            uiState.discount.endDate
                        ).ifEmpty { "No existe periodo" }
                    } else {
                        "No se pudo determinar"
                    }
                val remainingStartEndTimeText =
                    if (uiState.discount.startDate != null && uiState.discount.endDate != null) {
                        TimeUtils.getRemainingTimeString(
                            uiState.discount.startDate.toLocalTime(),
                            uiState.discount.endDate.toLocalTime()
                        )
                    } else {
                        ""
                    }
                val createdAt = uiState.discount.createdAt?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"
                val updatedAt = uiState.discount.updatedAt?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"
                val startAt = uiState.discount.startDate?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"
                val endAt = uiState.discount.endDate?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(top = 20.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.discount),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Nombre",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = uiState.discount.name,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Porcentaje",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = "$percentage%",
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Inicio",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = startAt,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Fin",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = endAt,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Periodo de",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = "$remainingStartEndText $remainingStartEndTimeText",
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Creacion",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = createdAt,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Actualizacion",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                                Text(
                                    text = updatedAt,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                        }
                    }
                    if (products.itemCount == 0) {
                        DiscountTabVisualizerItemDetailedProductEmpty()
                    } else {
                        when (products.loadState.refresh) {
                            is LoadState.Error -> {

                            }

                            LoadState.Loading -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .heightIn(max = 1000.dp)
                                ) {
                                    items(DISCOUNT_TAB_VISUALIZER_ITEM_DETAILED_SHIMMER_ITEM_COUNT) {
                                        DiscountTabVisualizerItemDetailedProductShimmer()
                                    }
                                }
                            }

                            is LoadState.NotLoading -> {
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
                                            DiscountTabVisualizerItemDetailedProduct(productDomain = product)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // TODO
            }
        }
    }
}