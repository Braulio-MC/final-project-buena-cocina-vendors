package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.add

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.core.TimeUtils
import com.bmc.buenacocinavendors.ui.screen.common.TimePickerDialog
import com.bmc.buenacocinavendors.ui.viewmodel.DiscountTabAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountTabAdd(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    startDatePickerState: DatePickerState = rememberDatePickerState(),
    startTimePickerState: TimePickerState = rememberTimePickerState(),
    endDatePickerState: DatePickerState = rememberDatePickerState(),
    endTimePickerState: TimePickerState = rememberTimePickerState(),
    viewModel: DiscountTabAddViewModel = hiltViewModel(
        creationCallback = { factory: DiscountTabAddViewModel.DiscountTabAddViewModelFactory ->
            factory.create(storeId)
        }
    ),
    onSuccessfulCreation: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    var showStartDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var showStartTimePickerDialog by remember {
        mutableStateOf(false)
    }
    var showEndDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var showEndTimePickerDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is DiscountTabAddViewModel.ValidationEvent.Failure -> {
                    Log.e("DiscountTabAdd", "Error: ${event.error}")
                }

                DiscountTabAddViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Descuento creado con exito",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulCreation()
                    }
                }
            }
        }
    }

    DiscountTabAddContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        startDatePickerState = startDatePickerState,
        showStartDatePickerDialog = showStartDatePickerDialog,
        onShowStartDatePickerDialogChanged = { showStartDatePickerDialog = it },
        startTimePickerState = startTimePickerState,
        showStartTimePickerDialog = showStartTimePickerDialog,
        onShowStartTimePickerDialogChanged = { showStartTimePickerDialog = it },
        endDatePickerState = endDatePickerState,
        showEndDatePickerDialog = showEndDatePickerDialog,
        onShowEndDatePickerDialogChanged = { showEndDatePickerDialog = it },
        endTimePickerState = endTimePickerState,
        showEndTimePickerDialog = showEndTimePickerDialog,
        onShowEndTimePickerDialogChanged = { showEndTimePickerDialog = it },
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountTabAddContent(
    windowSizeClass: WindowSizeClass,
    uiState: DiscountTabAddUiState,
    scrollState: ScrollState,
    startDatePickerState: DatePickerState,
    showStartDatePickerDialog: Boolean,
    onShowStartDatePickerDialogChanged: (Boolean) -> Unit,
    startTimePickerState: TimePickerState,
    showStartTimePickerDialog: Boolean,
    onShowStartTimePickerDialogChanged: (Boolean) -> Unit,
    endDatePickerState: DatePickerState,
    showEndDatePickerDialog: Boolean,
    onShowEndDatePickerDialogChanged: (Boolean) -> Unit,
    endTimePickerState: TimePickerState,
    showEndTimePickerDialog: Boolean,
    onShowEndTimePickerDialogChanged: (Boolean) -> Unit,
    onIntent: (DiscountTabAddIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val finalStartDate = uiState.startDate?.let {
            DateUtils.localDateTimeToStringWithoutTime(it)
        } ?: ""
        val finalEndDate = uiState.endDate?.let {
            DateUtils.localDateTimeToStringWithoutTime(it)
        } ?: ""
        val finalStartTime = uiState.startTime?.let {
            TimeUtils.localTimeToString(it)
        } ?: ""
        val finalEndTime = uiState.endTime?.let {
            TimeUtils.localTimeToString(it)
        } ?: ""

        Text(
            text = "Nombre",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.name,
            onValueChange = { newName ->
                onIntent(DiscountTabAddIntent.NameChanged(newName))
            },
            placeholder = {
                Text(text = "ej. Descuento especial")
            },
            singleLine = true,
            isError = uiState.nameError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (uiState.nameError != null) {
            Text(
                text = uiState.nameError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Porcentaje de descuento",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.percentage,
            onValueChange = { newPercentage ->
                onIntent(DiscountTabAddIntent.PercentageChanged(newPercentage))
            },
            placeholder = {
                Text(text = "ej. 35")
            },
            singleLine = true,
            isError = uiState.percentageError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (uiState.percentageError != null) {
            Text(
                text = uiState.percentageError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Fecha de inicio",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elegir fecha",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { onShowStartDatePickerDialogChanged(true) },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
                if (uiState.startDate != null) {
                    Text(
                        text = finalStartDate,
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "No se ha definido una fecha",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (showStartDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { onShowStartDatePickerDialogChanged(false) },
                        dismissButton = {
                            Button(onClick = { onShowStartDatePickerDialogChanged(false) }) {
                                Text(text = "Cancelar")
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                startDatePickerState.selectedDateMillis?.let {
                                    onIntent(DiscountTabAddIntent.StartDateChanged(it))
                                }
                                onShowStartDatePickerDialogChanged(false)
                            }) {
                                Text(text = "Aceptar")
                            }
                        }
                    ) {
                        DatePicker(state = startDatePickerState)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Hora de inicio",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elegir hora",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { onShowStartTimePickerDialogChanged(true) },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
                if (uiState.startTime != null) {
                    Text(
                        text = finalStartTime,
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "No se ha definido una hora",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (showStartTimePickerDialog) {
                    TimePickerDialog(
                        onCancel = { onShowStartTimePickerDialogChanged(false) },
                        onConfirm = {
                            onIntent(
                                DiscountTabAddIntent.StartTimeChanged(
                                    startTimePickerState.hour,
                                    startTimePickerState.minute
                                )
                            )
                            onShowStartTimePickerDialogChanged(false)
                        }
                    ) {
                        TimePicker(state = startTimePickerState)
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Fecha de fin",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elegir fecha",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { onShowEndDatePickerDialogChanged(true) },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
                if (uiState.endDate != null) {
                    Text(
                        text = finalEndDate,
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "No se ha definido una fecha",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (showEndDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { onShowEndDatePickerDialogChanged(false) },
                        dismissButton = {
                            Button(onClick = { onShowEndDatePickerDialogChanged(false) }) {
                                Text(text = "Cancelar")
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                endDatePickerState.selectedDateMillis?.let {
                                    onIntent(DiscountTabAddIntent.EndDateChanged(it))
                                }
                                onShowEndDatePickerDialogChanged(false)
                            }) {
                                Text(text = "Aceptar")
                            }
                        }
                    ) {
                        DatePicker(state = endDatePickerState)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Hora de fin",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elegir hora",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(
                        onClick = { onShowEndTimePickerDialogChanged(true) },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
                if (uiState.endTime != null) {
                    Text(
                        text = finalEndTime,
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "No se ha definido una hora",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (showEndTimePickerDialog) {
                    TimePickerDialog(
                        onCancel = { onShowEndTimePickerDialogChanged(false) },
                        onConfirm = {
                            onIntent(
                                DiscountTabAddIntent.EndTimeChanged(
                                    endTimePickerState.hour,
                                    endTimePickerState.minute
                                )
                            )
                            onShowEndTimePickerDialogChanged(false)
                        }
                    ) {
                        TimePicker(state = endTimePickerState)
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inicio establecido",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (uiState.startDate != null && uiState.startTime != null) {
                    Text(
                        text = "$finalStartDate $finalStartTime",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = "No se ha definido un inicio",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fin establecido",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (uiState.endDate != null && uiState.endTime != null) {
                    Text(
                        text = "$finalEndDate $finalEndTime",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = "No se ha definido un fin",
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        if (uiState.dateConsistencyError != null) {
            Text(
                text = uiState.dateConsistencyError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = {
                onIntent(DiscountTabAddIntent.Submit)
            },
            enabled = !uiState.isWaitingForResult,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.CenterHorizontally)
                .size(200.dp, 50.dp)
        ) {
            if (uiState.isWaitingForResult) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(20.dp),
                )
            } else {
                Text(
                    text = "Agregar",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}