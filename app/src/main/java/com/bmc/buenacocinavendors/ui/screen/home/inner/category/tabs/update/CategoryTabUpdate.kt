package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.update

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.bmc.buenacocinavendors.core.CATEGORY_TAB_UPDATE_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.CategoryEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.CategoryItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.CategoryTabUpdateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryTabUpdate(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    snackbarHostState: SnackbarHostState,
    scrollState: ScrollState = rememberScrollState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: CategoryTabUpdateViewModel = hiltViewModel(
        creationCallback = { factory: CategoryTabUpdateViewModel.CategoryTabUpdateViewModelFactory ->
            factory.create(storeId)
        }
    ),
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    onSuccessfulUpdate: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is CategoryTabUpdateViewModel.ValidationEvent.Failure -> {
                    Log.e("CategoryTabUpdate", "Error: ${event.error}")
                }

                CategoryTabUpdateViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Categoria actualizada con exito",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulUpdate()
                    }
                }
            }
        }
    }

    CategoryTabUpdateContent(
        windowSizeClass = windowSizeClass,
        snackbarHostState = snackbarHostState,
        uiState = uiState.value,
        scrollState = scrollState,
        coroutineScope = coroutineScope,
        categories = categories,
        generalCategories = generalCategories,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun CategoryTabUpdateContent(
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    uiState: CategoryTabUpdateUiState,
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    onIntent: (CategoryTabUpdateIntent) -> Unit,
) {
    val currentCategoryUpdateParentName =
        uiState.currentCategoryUpdate?.parent?.name?.ifEmpty { "Sin supercategoria" }
            ?: "Sin supercategoria"
    val currentParentNewName = uiState.currentParent?.name ?: "No se actualizara"

    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "¿Que categoria actualizar?",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (categories.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.currentCategoryUpdate != null) {
                    onIntent(CategoryTabUpdateIntent.CategoryUpdateChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(CATEGORY_TAB_UPDATE_SHIMMER_ITEM_COUNT) {
                        CategoryItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (categories.itemCount == 0) {
                    CategoryEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = categories.itemCount,
                            key = categories.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val category = categories[index]
                            if (category != null) {
                                CategoryTabUpdateMyCategoryItem(
                                    category = category,
                                    onClick = { cat ->
                                        onIntent(CategoryTabUpdateIntent.CategoryUpdateChanged(cat))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.currentCategoryUpdateError != null) {
            Text(
                text = uiState.currentCategoryUpdateError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Text(
            text = "Nombre para la categoria",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = uiState.name,
            onValueChange = { newName ->
                onIntent(CategoryTabUpdateIntent.NameChanged(newName))
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
        Text(
            text = "Supercategoria",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.currentParent != null) {
            if (uiState.currentParent.id == uiState.currentCategoryUpdate?.id) {
                onIntent(CategoryTabUpdateIntent.ParentChanged())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categoria elegida",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = uiState.currentParent.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .widthIn(0.dp, 250.dp)
                )
                IconButton(
                    onClick = {
                        onIntent(CategoryTabUpdateIntent.ParentChanged())
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                    )
                }
            }
        } else {
            Text(
                text = makeBulletedList(items = listOf("No se ha elegido una supercategoria")),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
            )
        }
        Text(
            text = "De tu conjunto de categorias",
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        )
        when (categories.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.currentParent != null) {
                    onIntent(CategoryTabUpdateIntent.ParentChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(CATEGORY_TAB_UPDATE_SHIMMER_ITEM_COUNT) {
                        CategoryItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (categories.itemCount == 0) {
                    CategoryEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = categories.itemCount,
                            key = categories.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val category = categories[index]
                            if (category != null) {
                                CategoryTabUpdateMyCategoryItem(
                                    category = category,
                                    onClick = { cat ->
                                        if (cat.id == uiState.currentCategoryUpdate?.id) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "No se puede seleccionar la misma categoria",
                                                    withDismissAction = true,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            onIntent(CategoryTabUpdateIntent.ParentChanged(cat))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = "Del conjunto general de categorias",
            fontSize = 18.sp,
            fontWeight = FontWeight.W500
        )
        when (generalCategories.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.currentParent != null) {
                    onIntent(CategoryTabUpdateIntent.ParentChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(CATEGORY_TAB_UPDATE_SHIMMER_ITEM_COUNT) {
                        CategoryItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (generalCategories.itemCount == 0) {
                    CategoryEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = generalCategories.itemCount,
                            key = generalCategories.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val category = generalCategories[index]
                            if (category != null) {
                                CategoryTabUpdateGeneralCategoryItem(
                                    category = category,
                                    onClick = { cat ->
                                        onIntent(CategoryTabUpdateIntent.ParentChanged(cat))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = "Cambios",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.currentCategoryUpdate != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.currentCategoryUpdate.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                )
                Text(
                    text = uiState.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentCategoryUpdateParentName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.3f)
                )
                Text(
                    text = currentParentNewName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                )
            }
        } else {
            Text(
                text = makeBulletedList(items = listOf("No se han aplicado cambios")),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                onIntent(CategoryTabUpdateIntent.Submit)
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
                    text = "Actualizar",
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