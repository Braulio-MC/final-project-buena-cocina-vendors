package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.ui.screen.common.TimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreUpdateTimePickerSection(
    label: String,
    time: Pair<Int, Int>,
    showTimePicker: Boolean,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onShowTimePickerChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val timePickerState = remember(time) {
        TimePickerState(
            initialHour = time.first,
            initialMinute = time.second,
            is24Hour = true
        )
    }
    val timeStr =
        "${time.first.toString().padStart(2, '0')}:${time.second.toString().padStart(2, '0')}"

    LaunchedEffect(time) {
        if (!showTimePicker) {
            timePickerState.hour = time.first
            timePickerState.minute = time.second
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(2.dp))
        OutlinedButton(
            onClick = { onShowTimePickerChange(true) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = timeStr,
                    fontSize = 18.sp
                )
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Select hour",
                    modifier = Modifier.size(27.dp)
                )
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onCancel = {
                onShowTimePickerChange(false)
            },
            onConfirm = {
                onShowTimePickerChange(false)
                onConfirm(timePickerState.hour, timePickerState.minute)
            },
        ) {
            TimeInput(state = timePickerState)
        }
    }
}