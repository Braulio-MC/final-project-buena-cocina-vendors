package com.bmc.buenacocinavendors.ui.screen.order.detailed.rating

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.model.ProductReviewDomain
import java.text.DecimalFormat

@Composable
fun DetailedOrderRatingItemDialog(
    isDialogOpen: Boolean,
    scrollState: ScrollState = rememberScrollState(),
    productImage: String,
    productReview: ProductReviewDomain,
    onDismiss: () -> Unit,
) {
    if (isDialogOpen) {
        val rating = DecimalFormat("#.#").format(productReview.rating)
        val updatedAt = productReview.updatedAt?.let {
            DateUtils.localDateTimeToString(it)
        } ?: "No se pudo obtener la fecha"

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .height(450.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(productImage)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(130.dp)
                    )
                    Text(
                        text = "Se califico el producto con $rating de 5 estrellas",
                        textAlign = TextAlign.Center,
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 2.dp)
                            .height(160.dp)
                            .fillMaxWidth()
                            .border(width = 1.dp, color = Color.Gray)
                            .verticalScroll(scrollState)
                    ) {
                        Text(
                            text = productReview.comment,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Ultima actualizacions",
                            textAlign = TextAlign.End,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1.5f)
                        )
                        Text(
                            text = updatedAt,
                            textAlign = TextAlign.End,
                            color = Color.DarkGray,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.W500,
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
    }
}
