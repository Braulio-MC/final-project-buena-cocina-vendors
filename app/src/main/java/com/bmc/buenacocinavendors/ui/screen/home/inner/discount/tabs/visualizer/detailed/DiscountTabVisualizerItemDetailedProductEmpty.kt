package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.detailed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.R

@Composable
fun DiscountTabVisualizerItemDetailedProductEmpty() {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_products),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "No hay productos relacionados a este descuento",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.W500,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}