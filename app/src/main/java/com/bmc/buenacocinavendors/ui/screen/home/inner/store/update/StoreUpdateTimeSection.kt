package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StoreUpdateTimeSection(
    startTime: Pair<Int, Int>,
    endTime: Pair<Int, Int>,
    onOpeningTimeChange: (Int, Int) -> Unit,
    onClosingTimeChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var openingShowTimePicker by remember { mutableStateOf(false) }
    var closingShowTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StoreUpdateTimePickerSection(
            label = "Hora de apertura",
            time = startTime,
            showTimePicker = openingShowTimePicker,
            onConfirm = onOpeningTimeChange,
            onShowTimePickerChange = { show ->
                openingShowTimePicker = show
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        StoreUpdateTimePickerSection(
            label = "Hora de cierre",
            time = endTime,
            showTimePicker = closingShowTimePicker,
            onConfirm = onClosingTimeChange,
            onShowTimePickerChange = { show ->
                closingShowTimePicker = show
            },
            modifier = Modifier.weight(1f)
        )
    }
}