package com.bmc.buenacocinavendors.ui.screen.home.inner.store.visualizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.domain.model.ProductDomain
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Composable
fun StoreVisualizerItem(
    product: ProductDomain
) {
    var discountPercentage: BigDecimal? = null
    if (product.discount.startDate != null && product.discount.endDate != null) {
        if (
            product.discount.percentage != BigDecimal.ZERO
            && product.discount.startDate <= LocalDateTime.now()
            && product.discount.endDate > LocalDateTime.now()
        ) {
            discountPercentage = product.discount.percentage.setScale(2, RoundingMode.HALF_DOWN)
        }
    }
    val productRating =
        if (product.rating > BigDecimal.ZERO) product.rating.setScale(1, RoundingMode.HALF_DOWN)
            .toPlainString() else "N/A"

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp)
            .size(100.dp, 190.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(bottom = 5.dp)
            )
            Text(
                text = product.name,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp)
            )
            Text(
                text = "${product.categories.size} categorias",
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp, end = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "",
                    tint = colorResource(id = R.color.rating_star_filled)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = productRating,
                    fontSize = 17.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
                if (discountPercentage != null) {
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(id = R.color.store_visualizer_item_discount_color)
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .padding(end = 3.dp)
                            .wrapContentSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                .wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$discountPercentage%",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
