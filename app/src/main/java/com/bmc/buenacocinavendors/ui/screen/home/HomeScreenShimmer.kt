package com.bmc.buenacocinavendors.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bmc.buenacocinavendors.core.shimmerEffect

@Composable
@Preview(showBackground = true)
fun HomeScreenShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout() {
            val (storeImg, store) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .constrainAs(storeImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .shimmerEffect(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            )
            Row(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(start = 14.dp)
                        .weight(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp, 30.dp)
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .size(180.dp, 40.dp)
                            .shimmerEffect()
                    )
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shimmerEffect(CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(store) {
                        top.linkTo(storeImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(storeImg.bottom)
                    }
                    .shimmerEffect(RoundedCornerShape(20.dp))
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                .height(145.dp)
                .shimmerEffect(RoundedCornerShape(25.dp))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .height(80.dp)
                    .weight(0.25f)
                    .shimmerEffect()
            )
        }
    }
}