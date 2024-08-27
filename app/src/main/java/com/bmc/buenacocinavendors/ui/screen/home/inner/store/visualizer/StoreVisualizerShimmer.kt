package com.bmc.buenacocinavendors.ui.screen.home.inner.store.visualizer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bmc.buenacocinavendors.core.shimmerEffect

@Composable
@Preview(showBackground = true)
fun StoreVisualizerShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .height(250.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .size(250.dp, 45.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .size(200.dp, 70.dp)
                .shimmerEffect(RoundedCornerShape(15.dp))
        )
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(300.dp, 45.dp)
                .align(Alignment.Start)
                .shimmerEffect()
        )
    }
}