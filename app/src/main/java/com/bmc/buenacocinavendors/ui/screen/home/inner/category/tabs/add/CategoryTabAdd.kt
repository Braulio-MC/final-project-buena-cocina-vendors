package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmc.buenacocinavendors.ui.viewmodel.CategoryTabAddViewModel

@Composable
fun CategoryTabAdd(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    viewModel: CategoryTabAddViewModel = hiltViewModel(
        creationCallback = { factory: CategoryTabAddViewModel.CategoryTabAddViewModelFactory ->
            factory.create(storeId)
        }
    ),
    onSuccessfulCreation: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is CategoryTabAddViewModel.ValidationEvent.Failure -> {
                    Log.e("CategoryTabAdd", "Error: ${event.error}")
                }

                CategoryTabAddViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Categoria creada con exito",
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

    CategoryTabAddContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        scrollState = scrollState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun CategoryTabAddContent(
    windowSizeClass: WindowSizeClass,
    uiState: CategoryTabAddUiState,
    scrollState: ScrollState,
    onIntent: (CategoryTabAddIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Nombre de la categoria",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.name,
            onValueChange = { newName ->
                onIntent(CategoryTabAddIntent.NameChanged(newName))
            },
            placeholder = {
                Text(text = "ej. Bebida")
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
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                onIntent(CategoryTabAddIntent.Submit)
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