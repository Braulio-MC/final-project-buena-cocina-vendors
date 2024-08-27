package com.bmc.buenacocinavendors.ui.screen.home.inner.store.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmc.buenacocinavendors.core.shimmerEffect

@Composable
fun StoreUpdateShimmer(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Box(
            modifier = Modifier
                .size(300.dp, 30.dp)
                .padding(bottom = 5.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
                .shimmerEffect(RoundedCornerShape(10.dp))
        )
        Box(
            modifier = Modifier
                .size(210.dp, 40.dp)
                .align(Alignment.CenterHorizontally)
                .shimmerEffect(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .size(200.dp, 30.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .size(200.dp, 30.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .size(200.dp, 30.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .size(200.dp, 30.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(200.dp, 50.dp)
                .align(Alignment.CenterHorizontally)
                .shimmerEffect(RoundedCornerShape(10.dp))
        )
    }
}