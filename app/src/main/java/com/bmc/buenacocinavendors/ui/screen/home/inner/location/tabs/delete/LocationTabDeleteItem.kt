package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.delete

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.domain.model.LocationDomain

@Composable
fun LocationTabDeleteItem(
    location: LocationDomain,
    onClick: (LocationDomain) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(130.dp)
            .height(IntrinsicSize.Max)
            .clickable { onClick(location) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = location.name,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}