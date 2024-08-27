package com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.detailed

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmc.buenacocinavendors.core.shimmerEffect

@Composable
fun DiscountTabVisualizerItemDetailedProductShimmer() {
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
                .weight(1f)
                .shimmerEffect(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp, 25.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(1.dp, 20.dp)
                        .weight(1f)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .size(1.dp, 20.dp)
                        .weight(1.5f)
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(1.dp, 20.dp)
                        .weight(1f)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .size(1.dp, 20.dp)
                        .weight(1.5f)
                        .shimmerEffect()
                )
            }
        }
    }
}