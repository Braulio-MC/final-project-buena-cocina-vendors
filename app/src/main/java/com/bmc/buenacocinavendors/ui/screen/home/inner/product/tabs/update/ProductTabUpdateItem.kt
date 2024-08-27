package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import java.math.RoundingMode
import java.time.LocalDateTime

@Composable
fun ProductTabUpdateItem(
    product: ProductDomain,
    onClick: (ProductDomain) -> Unit
) {
    val currentContext = LocalContext.current
    val hasActiveDiscount =
        if (product.discount.startDate != null && product.discount.endDate != null) {
            val active = DateUtils.isInRange(
                LocalDateTime.now(),
                product.discount.startDate,
                product.discount.endDate
            )
            if (active) "Si" else "No"
        } else {
            "No se pudo determinar"
        }

    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(180.dp)
            .height(IntrinsicSize.Max)
            .clickable { onClick(product) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(currentContext)
                .data(product.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = product.name,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categoria",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.6f)
            )
            Text(
                text = product.category.name,
                textAlign = TextAlign.End,
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rebaja activa",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.8f)
            )
            Text(
                text = hasActiveDiscount,
                textAlign = TextAlign.End,
                color = Color.DarkGray,
                fontSize = 14.sp,
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

@Composable
@Preview(showBackground = true)
fun ProductTabUpdateItemPreview(){
    BuenaCocinaVendorsTheme {
        ProductTabUpdateItem(
            product = ProductDomain(
                id = "1",
                name = "Hamburguesa de res con papas",
                image = "",
                description = "Hamburguesa de res con papas fritas",
                price = BigDecimal(30.5).setScale(2, RoundingMode.HALF_DOWN),
                quantity = BigInteger("5"),
                discount = ProductDomain.ProductDiscountDomain(
                    id = "1",
                    percentage = BigDecimal(10.5).setScale(2, RoundingMode.HALF_DOWN),
                    startDate = LocalDateTime.of(2024, 8, 15, 0, 0),
                    endDate = LocalDateTime.of(2024, 8, 16, 0, 0)
                ),
                store = ProductDomain.ProductStoreDomain(
                    id = "1",
                    name = "La casa de la hamburguesa"
                ),
                category = ProductDomain.ProductCategoryDomain(
                    id = "1",
                    name = "Hamburger",
                    parentName = ""
                ),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            onClick = {}
        )
    }
}