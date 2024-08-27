package com.bmc.buenacocinavendors.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.ui.screen.common.NoInternetScreen
import com.bmc.buenacocinavendors.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onStoreUpdateButton: (String) -> Unit,
    onStoreVisualizerButton: (String) -> Unit,
    onCategoryButton: (String) -> Unit,
    onLocationButton: (String) -> Unit,
    onDiscountButton: (String) -> Unit,
    onProductButton: (String, String) -> Unit
) {
    val uiState = viewModel.store()
        .map { stores -> HomeUiState(store = stores.firstOrNull()) }
        .collectAsStateWithLifecycle(initialValue = HomeUiState(isLoading = true))
    val netState = viewModel.netState.collectAsStateWithLifecycle()

    HomeScreenContent(
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        netState = netState.value,
        scrollState = scrollState,
        onStoreUpdateButton = onStoreUpdateButton,
        onStoreVisualizerButton = onStoreVisualizerButton,
        onCategoryButton = onCategoryButton,
        onLocationButton = onLocationButton,
        onDiscountButton = onDiscountButton,
        onProductButton = onProductButton
    )
}

@Composable
fun HomeScreenContent(
    windowSizeClass: WindowSizeClass,
    uiState: HomeUiState,
    netState: NetworkStatus,
    scrollState: ScrollState,
    onStoreUpdateButton: (String) -> Unit,
    onStoreVisualizerButton: (String) -> Unit,
    onCategoryButton: (String) -> Unit,
    onLocationButton: (String) -> Unit,
    onDiscountButton: (String) -> Unit,
    onProductButton: (String, String) -> Unit
) {
    if (uiState.isLoading) {
        HomeScreenShimmer()
    } else {
        if (uiState.store != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConstraintLayout {
                    val (storeImg, store) = createRefs()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(245.dp)
                            .constrainAs(storeImg) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                            )
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .height(100.dp)
                                .padding(start = 14.dp)
                                .weight(0.7f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Bienvenido a",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W400,
                            )
                            Text(
                                text = uiState.store.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 14.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { }
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.store.image)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                            .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                            .constrainAs(store) {
                                top.linkTo(storeImg.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(storeImg.bottom)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                                .size(85.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(20.dp))
                                .clickable { onStoreUpdateButton(uiState.store.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Update,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                            )
                            Text(
                                text = "Actualizar",
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                                .size(85.dp)
                                .background(color = Color.Gray, shape = RoundedCornerShape(20.dp))
                                .clickable { onStoreVisualizerButton(uiState.store.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                            )
                            Text(
                                text = "Visualizar",
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .shadow(3.dp, shape = RoundedCornerShape(25.dp))
                        .height(145.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.LightGray,
                                    Color.Gray
                                )
                            ), shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { }
                ) {
                    val (img, text1, text2) = createRefs()

                    Image(
                        painter = painterResource(id = R.drawable.ads),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(end = 10.dp)
                            .constrainAs(img) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                    )
                    Text(
                        text = "¿Publicar un anuncio?",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .constrainAs(text1) {
                                top.linkTo(parent.top)
                                end.linkTo(img.start)
                                start.linkTo(parent.start)
                            }
                    )
                    Text(
                        text = "Haz clic para saber mas",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .constrainAs(text2) {
                                top.linkTo(text1.bottom)
                                end.linkTo(text1.end)
                                start.linkTo(text1.start)
                            }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(0.25f)
                            .clickable { onCategoryButton(uiState.store.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Category,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                .padding(5.dp)
                        )
                        Text(
                            text = "Categorias",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(0.25f)
                            .clickable { onLocationButton(uiState.store.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                .padding(5.dp)
                        )
                        Text(
                            text = "Direcciones",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(0.25f)
                            .clickable { onDiscountButton(uiState.store.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Discount,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                .padding(5.dp)
                        )
                        Text(
                            text = "Descuentos",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(0.25f)
                            .clickable { onProductButton(uiState.store.id, uiState.store.name) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Fastfood,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                .padding(5.dp)
                        )
                        Text(
                            text = "Productos",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        } else {
            if (netState == NetworkStatus.Unavailable || netState == NetworkStatus.Lost) {
                NoInternetScreen(
                    paddingValues = PaddingValues()
                )
            }
        }
    }
}