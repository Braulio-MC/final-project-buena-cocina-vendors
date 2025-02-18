package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.visualizer.detailed

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

@Composable
fun CategoryTabVisualizerItemProduct(
    productDomain: ProductDomain
) {
    val createdAt = productDomain.createdAt?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"
    val updatedAt = productDomain.updatedAt?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"

    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .weight(1f),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productDomain.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            Text(
                text = productDomain.name,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fecha de creacion",
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = createdAt,
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(end = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ultima actualizacion",
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = updatedAt,
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(end = 5.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CategoryTabVisualizerItemProductPreview() {
    BuenaCocinaVendorsTheme {
        CategoryTabVisualizerItemProduct(
            productDomain = ProductDomain(
                id = "1",
                name = "Hamburguesa de res y papas",
                description = "Esto es una descripcion",
                image = "",
                price = BigDecimal(55.6),
                updatedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                quantity = BigInteger("2"),
                discount = ProductDomain.ProductDiscountDomain(
                    id = "1",
                    percentage = BigDecimal(10.5),
                    startDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now(),
                ),
                store = ProductDomain.ProductStoreDomain(
                    id = "1",
                    name = "La Cuchara Verde",
                    ownerId = "123"
                ),
                rating = BigDecimal.ZERO,
                totalRating = BigDecimal.ZERO,
                totalReviews = BigInteger.ZERO,
                category = ProductDomain.ProductCategoryDomain(
                    id = "1",
                    name = "Hamburger",
                    parentName = ""
                )
            )
        )
    }
}