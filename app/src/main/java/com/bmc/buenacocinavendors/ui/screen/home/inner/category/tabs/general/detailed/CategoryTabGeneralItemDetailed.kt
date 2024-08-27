package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.general.detailed

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
import com.bmc.buenacocinavendors.core.CATEGORY_TAB_GENERAL_ITEM_DETAILED_SHIMMER_ITEM_COUNT
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.viewmodel.CategoryTabGeneralItemDetailedViewModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabGeneralItemDetailed(
    windowSizeClass: WindowSizeClass,
    categoryId: String,
    viewModel: CategoryTabGeneralItemDetailedViewModel = hiltViewModel(
        creationCallback = { factory: CategoryTabGeneralItemDetailedViewModel.CategoryTabGeneralItemDetailedViewModelFactory ->
            factory.create(categoryId)
        }
    ),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onBackButton: () -> Unit
) {
    val uiState = viewModel.category
        .map { category -> CategoryTabGeneralItemDetailedUiState(category = category) }
        .collectAsStateWithLifecycle(initialValue = CategoryTabGeneralItemDetailedUiState(isLoading = true))
    val products = viewModel.products().collectAsLazyPagingItems()

    CategoryTabItemDetailedContent(
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
fun CategoryTabItemDetailedContent(
    windowSizeClass: WindowSizeClass,
    scrollState: ScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: CategoryTabGeneralItemDetailedUiState,
    products: LazyPagingItems<ProductDomain>,
    onBackButton: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalles de la categoria",
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
            CategoryTabGeneralItemDetailedShimmer(paddingValues)
        } else {
            if (uiState.category != null) {
                val createdAt = uiState.category.createdAt?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"
                val updatedAt = uiState.category.updatedAt?.let {
                    DateUtils.localDateTimeToString(it)
                } ?: "No se pudo obtener la fecha"
                val parentCategoryName =
                    uiState.category.parent.name.ifEmpty { "Sin supercategoria" }

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
                            painter = painterResource(id = R.drawable.category),
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
                                        .weight(1f)
                                )
                                Text(
                                    text = uiState.category.name,
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
                                    text = "Es subcategoria de",
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
                                    text = parentCategoryName,
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
                                    text = "Fecha de creacion",
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
                                    text = "Ultima actualizacion",
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
                                        .weight(1f)
                                        .padding(end = 5.dp)
                                )
                            }
                        }
                    }
                    when (products.loadState.refresh) {
                        is LoadState.Error -> {

                        }

                        LoadState.Loading -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .heightIn(max = 1000.dp)
                            ) {
                                items(CATEGORY_TAB_GENERAL_ITEM_DETAILED_SHIMMER_ITEM_COUNT) {
                                    CategoryTabGeneralItemProductShimmer()
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            if (products.itemCount == 0) {
                                CategoryTabGeneralItemProductsEmpty(categoryDomain = uiState.category)
                            } else {
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
                                    ) {
                                        val product = products[it]
                                        if (product != null) {
                                            CategoryTabGeneralItemProduct(product)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}