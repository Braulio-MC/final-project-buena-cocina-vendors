package com.bmc.buenacocinavendors.ui.screen.home.inner.product.tabs.update

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

@Composable
fun ProductTabUpdateDiscountItem(
    discount: DiscountDomain,
    onClick: (DiscountDomain) -> Unit
) {
    val startDate = discount.startDate?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"
    val endDate = discount.endDate?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"

    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(200.dp)
            .height(IntrinsicSize.Max)
            .clickable { onClick(discount) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.Discount,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = discount.name,
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
                text = "Inicio",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.5f)
            )
            Text(
                text = startDate,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
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
                text = "Fin",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(0.5f)
            )
            Text(
                text = endDate,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 14.sp,
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