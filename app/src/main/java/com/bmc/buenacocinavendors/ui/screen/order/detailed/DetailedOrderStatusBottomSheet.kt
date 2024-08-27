package com.bmc.buenacocinavendors.ui.screen.order.detailed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmc.buenacocinavendors.core.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedOrderStatusBottomSheet(
    sheetState: SheetState,
    onDetailedOrderStatusBottomSheetItemClick: (OrderStatus) -> Unit,
    onDismissRequest: () -> Unit
) {
    val discardedStatus = listOf(OrderStatus.ERROR.status, OrderStatus.UNASSIGNED.status)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(OrderStatus.entries) { item ->
                if (item.status !in discardedStatus) {
                    DetailedOrderStatusBottomSheetItem(
                        orderStatus = item,
                        onClick = onDetailedOrderStatusBottomSheetItemClick
                    )
                }
            }
        }
    }
}