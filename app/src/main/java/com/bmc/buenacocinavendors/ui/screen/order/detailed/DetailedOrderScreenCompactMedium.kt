package com.bmc.buenacocinavendors.ui.screen.order.detailed

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.core.getOrderStatusColor
import com.bmc.buenacocinavendors.core.getOrderStatusLevel
import com.bmc.buenacocinavendors.ui.screen.common.NoInternetScreen
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedOrderScreenCompactMedium(
    uiState: DetailedOrderUiState,
    snackbarHostState: SnackbarHostState,
    netState: NetworkStatus,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (DetailedOrderIntent) -> Unit,
    onOrderStatusClick: () -> Unit,
    onOrderRatedButton: () -> Unit,
    onBackButton: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalles del pedido",
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
                actions = {
                    IconButton(
                        onClick = { onIntent(DetailedOrderIntent.CreateChannel) },
                        enabled = uiState.order != null && !uiState.isWaitingForChannelResult
                    ) {
                        if (uiState.isWaitingForChannelResult) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Chat,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { onOrderRatedButton() },
                        enabled = uiState.order != null &&
                        uiState.order.status == OrderStatus.DELIVERED.status &&
                        uiState.order.rated
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.StarRate,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        if (uiState.order != null) {
            val gridItems = uiState.lines.take(4)
            val orderStoreName = uiState.order.store.name
            val orderDesc =
                if (uiState.lines.size > 1) "Multiples productos de $orderStoreName" else "Producto de $orderStoreName"
            val createdAt = uiState.order.createdAt?.let {
                DateUtils.localDateTimeToString(it)
            } ?: "No se pudo obtener la fecha"
            val updatedAt = uiState.order.updatedAt?.let {
                DateUtils.localDateTimeToString(it)
            } ?: "No se pudo obtener la fecha"
            val orderLocation = uiState.order.deliveryLocation.name
            val orderPaymentMethod = uiState.order.paymentMethod.name
            val elapsedTimeStatus = if (uiState.order.status != OrderStatus.DELIVERED.status) {
                if (uiState.order.createdAt != null) {
                    val elapsedTime =
                        DateUtils.getElapsedTime(uiState.order.createdAt, LocalDateTime.now())
                    "$elapsedTime minutos"
                } else {
                    "No se pudo obtener"
                }
            } else {
                "Se realizo la entrega"
            }
            val orderStatusLevel = getOrderStatusLevel(uiState.order.status)

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(3.dp),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(200.dp)
                    ) {

                    }
                    Text(
                        text = orderDesc,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    )
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
                                .padding(10.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
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
                                        .weight(1f)
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
                                        .weight(2f)
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
                                        .weight(1f)
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
                                        .weight(2f)
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
                                    text = "Total",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = "$${uiState.orderTotal}",
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(2f)
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
                                    text = "Direccion",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = orderLocation,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(2f)
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
                                    text = "Metodo de pago",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = orderPaymentMethod,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(2f)
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
                                    text = "Tiempo transcurrido",
                                    textAlign = TextAlign.Start,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = elapsedTimeStatus,
                                    textAlign = TextAlign.End,
                                    color = Color.DarkGray,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(end = 5.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "Productos en el pedido",
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
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
                        items(uiState.lines) { item ->
                            DetailedOrderItemCompactMedium(line = item)
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Card(elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(id = getOrderStatusColor(uiState.status.status))
                            ),
                            modifier = Modifier
                                .size(150.dp, 40.dp)
                                .minimumInteractiveComponentSize()
                                .clickable { onOrderStatusClick() }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.status.status,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Button(
                            onClick = {
                                onIntent(DetailedOrderIntent.Submit)
                            },
                            modifier = Modifier
                                .size(185.dp, 45.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = uiState.status.level > orderStatusLevel
                        ) {
                            if (uiState.isWaitingForStatusResult) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .size(20.dp),
                                )
                            } else {
                                Text(
                                    text = "Actualizar estado",
                                    textAlign = TextAlign.Center,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (netState == NetworkStatus.Unavailable || netState == NetworkStatus.Lost) {
                NoInternetScreen(
                    paddingValues = paddingValues
                )
            }
        }
    }
}