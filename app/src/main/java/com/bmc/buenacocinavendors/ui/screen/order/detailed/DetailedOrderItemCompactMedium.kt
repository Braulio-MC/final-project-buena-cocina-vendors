package com.bmc.buenacocinavendors.ui.screen.order.detailed

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
import androidx.compose.material3.HorizontalDivider
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
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.time.LocalDateTime

@Composable
fun DetailedOrderItemCompactMedium(
    line: OrderLineDomain
) {
    val total =
        (line.quantity.toBigDecimal() * line.product.price).setScale(2, RoundingMode.HALF_DOWN)

    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(line.product.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            Text(
                text = line.product.name,
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Por unidad",
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
                    text = "$${line.product.price.toPlainString()}",
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cantidad",
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
                    text = line.quantity.toString(),
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 5.dp)
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 3.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
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
                    text = "$$total",
                    textAlign = TextAlign.End,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 5.dp)

                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DetailedOrderItemCompactMediumPreview() {
    BuenaCocinaVendorsTheme {
        DetailedOrderItemCompactMedium(
            line = OrderLineDomain(
                id = "1",
                quantity = BigInteger("4"),
                product = OrderLineDomain.OrderLineProductDomain(
                    id = "1",
                    name = "Hamburguesa de res con papas",
                    description = "Hamburguesa de res con papas fritas",
                    image = "",
                    price = BigDecimal(150.50).setScale(2),
                    discount = OrderLineDomain.OrderLineProductDomain.OrderLineProductDiscountDomain(
                        id = "1",
                        percentage = BigDecimal(10),
                        startDate = LocalDateTime.now(),
                        endDate = LocalDateTime.now()
                    )
                ),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }
}