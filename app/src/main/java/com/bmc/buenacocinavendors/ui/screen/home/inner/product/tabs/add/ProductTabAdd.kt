package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.add

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.core.PRODUCT_TAB_ADD_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.makeBulletedList
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.domain.model.DiscountDomain
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.CategoryEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.CategoryItemShimmer
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add.CategoryTabAddIntent
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.DiscountEmpty
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.DiscountItemShimmer
import com.bmc.buenacocinavendors.ui.viewmodel.ProductTabAddViewModel

@Composable
fun ProductTabAdd(
    windowSizeClass: WindowSizeClass,
    storeId: String,
    storeName: String,
    snackbarHostState: SnackbarHostState,
    viewModel: ProductTabAddViewModel = hiltViewModel(
        creationCallback = { factory: ProductTabAddViewModel.ProductTabAddViewModelFactory ->
            factory.create(storeId, storeName)
        }
    ),
    scrollState: ScrollState = rememberScrollState(),
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    discounts: LazyPagingItems<DiscountDomain>,
    defaultDiscounts: List<DiscountDomain>?,
    onSuccessfulCreation: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val defaultDiscount = defaultDiscounts?.firstOrNull()
    val currentContext = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.onIntent(ProductTabAddIntent.ImageChanged(uri))
        }
    )

    LaunchedEffect(key1 = currentContext) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is ProductTabAddViewModel.ValidationEvent.Failure -> {
                    Log.e("ProductTabAdd", "ProductTabAdd: ${event.error}")
                }

                ProductTabAddViewModel.ValidationEvent.Success -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Producto creado",
                        withDismissAction = true
                    )
                    if (result == SnackbarResult.Dismissed || result == SnackbarResult.ActionPerformed) {
                        onSuccessfulCreation()
                    }
                }
            }
        }
    }

    ProductTabAddContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        categories = categories,
        generalCategories = generalCategories,
        discounts = discounts,
        defaultDiscount = defaultDiscount,
        scrollState = scrollState,
        onIntent = viewModel::onIntent,
        onPickPhotoButton = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}

@Composable
fun ProductTabAddContent(
    windowSizeClass: WindowSizeClass,
    uiState: ProductTabAddUiState,
    categories: LazyPagingItems<CategoryDomain>,
    generalCategories: LazyPagingItems<CategoryDomain>,
    discounts: LazyPagingItems<DiscountDomain>,
    defaultDiscount: DiscountDomain?,
    scrollState: ScrollState,
    onIntent: (ProductTabAddIntent) -> Unit,
    onPickPhotoButton: () -> Unit
) {
    val currentContext = LocalContext.current

    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Imagen del producto",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(currentContext)
                    .data(uiState.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(135.dp)
            )
        }
        Button(
            onClick = onPickPhotoButton,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Seleccionar imagen",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (uiState.imageError != null) {
            Text(
                text = uiState.imageError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))
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
                onIntent(ProductTabAddIntent.NameChanged(newName))
            },
            placeholder = {
                Text(text = "ej. Hamburguesa")
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
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Descripcion",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            value = uiState.description,
            onValueChange = { newDescription ->
                onIntent(ProductTabAddIntent.DescriptionChanged(newDescription))
            },
            placeholder = {
                Text(text = "ej. Con tomate, lechuga, cebolla y papas fritas")
            },
            isError = uiState.descriptionError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (uiState.descriptionError != null) {
            Text(
                text = uiState.descriptionError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Precio",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.price,
                    onValueChange = { newPrice ->
                        onIntent(ProductTabAddIntent.PriceChanged(newPrice))
                    },
                    placeholder = {
                        Text(text = "ej. 50.5")
                    },
                    singleLine = true,
                    isError = uiState.priceError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (uiState.priceError != null) {
                    Text(
                        text = uiState.priceError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Cantidad",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.quantity,
                    onValueChange = { newQuantity ->
                        onIntent(ProductTabAddIntent.QuantityChanged(newQuantity))
                    },
                    placeholder = {
                        Text(text = "ej. 5")
                    },
                    singleLine = true,
                    isError = uiState.quantityError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (uiState.quantityError != null) {
                    Text(
                        text = uiState.quantityError.asString(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Categoria",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.category != null) {
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
                    text = uiState.category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .widthIn(0.dp, 250.dp)
                )
                IconButton(
                    onClick = {
                        onIntent(ProductTabAddIntent.CategoryChanged())
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
                text = makeBulletedList(items = listOf("No se ha elegido una categoria")),
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
                if (uiState.category != null) {
                    onIntent(ProductTabAddIntent.CategoryChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(PRODUCT_TAB_ADD_SHIMMER_ITEM_COUNT) {
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
                                ProductTabAddMyCategoryItem(
                                    category = category,
                                    onClick = {
                                        onIntent(ProductTabAddIntent.CategoryChanged(it))
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
                if (uiState.category != null) {
                    onIntent(ProductTabAddIntent.CategoryChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(PRODUCT_TAB_ADD_SHIMMER_ITEM_COUNT) {
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
                                ProductTabAddGeneralCategoryItem(
                                    category = category,
                                    onClick = {
                                        onIntent(ProductTabAddIntent.CategoryChanged(it))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.categoryError != null) {
            Text(
                text = uiState.categoryError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Descuento",
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (uiState.discount != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Descuento elegido",
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
                    text = uiState.discount.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .widthIn(0.dp, 250.dp)
                )
                IconButton(
                    onClick = {
                        onIntent(ProductTabAddIntent.DiscountChanged())
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
                text = makeBulletedList(items = listOf("No se ha elegido un descuento")),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
            )
        }
        when (discounts.loadState.refresh) {
            is LoadState.Error -> {

            }

            LoadState.Loading -> {
                if (uiState.discount != null) {
                    onIntent(ProductTabAddIntent.DiscountChanged())
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(PRODUCT_TAB_ADD_SHIMMER_ITEM_COUNT) {
                        DiscountItemShimmer()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (discounts.itemCount == 0) {
                    DiscountEmpty()
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            count = discounts.itemCount,
                            key = discounts.itemKey { item ->
                                item.id
                            }
                        ) { index ->
                            val discount = discounts[index]
                            if (discount != null) {
                                ProductTabAddDiscountItem(
                                    discount = discount,
                                    onClick = {
                                        onIntent(ProductTabAddIntent.DiscountChanged(it))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Usar el descuento por defecto",
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .weight(1f)
            )
            Checkbox(
                checked = uiState.useDefaultDiscount,
                onCheckedChange = {
                    onIntent(
                        ProductTabAddIntent.DefaultDiscountChanged(it, defaultDiscount)
                    )
                }
            )
        }
        if (uiState.discountError != null) {
            Text(
                text = uiState.discountError.asString(),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onIntent(ProductTabAddIntent.Submit)
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