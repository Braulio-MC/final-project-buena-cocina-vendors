package com.bmc.buenacocinavendors.ui.screen.order.detailed.rating

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.bmc.buenacocinavendors.domain.model.ProductReviewDomain
import java.text.DecimalFormat

@Composable
fun DetailedOrderRatingItem(
    line: OrderLineDomain,
    productReview: ProductReviewDomain
) {
    val rating = DecimalFormat("#.#").format(productReview.rating)
    var isItemRatingDialogOpen by remember { mutableStateOf(false) }

    DetailedOrderRatingItemDialog(
        isDialogOpen = isItemRatingDialogOpen,
        productImage = line.product.image,
        productReview = productReview,
        onDismiss = { isItemRatingDialogOpen = false }
    )

    Row(
        modifier = Modifier
            .padding(3.dp)
            .height(125.dp)
            .fillMaxWidth()
            .clickable { isItemRatingDialogOpen = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(line.product.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(5.dp)
                .size(95.dp)
                .weight(0.8f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .weight(2f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = line.product.name,
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "$rating/5",
                textAlign = TextAlign.Start,
                fontSize = 16.5.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = productReview.comment,
                textAlign = TextAlign.Start,
                fontSize = 14.5.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}