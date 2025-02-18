package com.bmc.buenacocinavendors.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import com.bmc.buenacocinavendors.core.bubbleShape
import com.bmc.buenacocinavendors.domain.model.InsightCalculateSalesByDayOfWeekDomain
import com.bmc.buenacocinavendors.domain.toPx
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreenWeeklySalesBarChart(
    modifier: Modifier = Modifier,
    isWeeklyMappedSalesLoading: Boolean,
    weeklyMappedSalesState: List<Pair<String, InsightCalculateSalesByDayOfWeekDomain?>>
) {
    var selectedDay by remember { mutableStateOf<InsightCalculateSalesByDayOfWeekDomain?>(null) }
    var barEntryOffset by remember { mutableStateOf<IntOffset?>(null) }

    HomeScreenWeeklySalesBarChartContent(
        modifier = modifier,
        isWeeklyMappedSalesLoading = isWeeklyMappedSalesLoading,
        weekDays = weeklyMappedSalesState.map { it.first },
        weekSales = weeklyMappedSalesState.map { it.second },
        selectedDay = selectedDay,
        barEntryOffset = barEntryOffset,
        onChartBarEntrySelected = { index, x, y ->
            val sale = weeklyMappedSalesState[index].second
            if (selectedDay != sale) {
                selectedDay = sale
                barEntryOffset = IntOffset(x, y)
            }
        },
        onChartBarEntryUnselected = {
            selectedDay = null
            barEntryOffset = null
        }
    )
}

@Composable
fun HomeScreenWeeklySalesBarChartContent(
    modifier: Modifier = Modifier,
    isWeeklyMappedSalesLoading: Boolean,
    weekDays: List<String>,
    weekSales: List<InsightCalculateSalesByDayOfWeekDomain?>,
    selectedDay: InsightCalculateSalesByDayOfWeekDomain?,
    barEntryOffset: IntOffset?,
    onChartBarEntrySelected: (Int, Int, Int) -> Unit,
    onChartBarEntryUnselected: () -> Unit
) {
    val popUpInnerBoxWidth = 110
    val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))
    val sevenDaysAgo = LocalDate.now().minusDays(7)
        .format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ventas semanales",
                textAlign = TextAlign.Start,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 15.dp, end = 15.dp)
            )
            Text(
                text = "Las ventas se presentan en cantidad de ordenes hechas",
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 15.dp, end = 15.dp)
            )
            if (isWeeklyMappedSalesLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    factory = { context ->
                        BarChart(context).apply {
                            setDrawGridBackground(false)
                            setFitBars(true)
                            isHighlightPerDragEnabled = false
                            description.isEnabled = false
                            axisRight.isEnabled = false
                            legend.isEnabled = false
                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                granularity = 1f
                                valueFormatter = IndexAxisValueFormatter(weekDays)
                                textSize = 16f
                            }
                            axisLeft.apply {
                                setDrawGridLines(true)
                                gridLineWidth = 1f
                                axisMinimum = 0f
                                textSize = 16f
                            }
                            animateY(1000, Easing.EaseInOutQuad)
                        }
                    },
                    update = { chart ->
                        chart.setOnChartValueSelectedListener(object :
                            OnChartValueSelectedListener {
                            override fun onValueSelected(e: Entry?, h: Highlight?) {
                                if (e is BarEntry) {
                                    val index = e.x.toInt()
                                    val transformer =
                                        chart.getTransformer(YAxis.AxisDependency.LEFT)
                                    val pixelValues = floatArrayOf(e.x, e.y)
                                    transformer.pointValuesToPixel(pixelValues)
                                    val centerX = pixelValues[0] + (chart.barData.barWidth / 2)
                                    val centerY = pixelValues[1] / 2
                                    onChartBarEntrySelected(
                                        index,
                                        (centerX - chart.context.toPx(popUpInnerBoxWidth) / 2).toInt(),
                                        centerY.toInt()
                                    )
                                }
                            }

                            override fun onNothingSelected() {
                                onChartBarEntryUnselected()
                            }
                        })
                        val entries = weekSales.mapIndexed { index, value ->
                            BarEntry(index.toFloat(), value?.dailyOrderCount?.toFloat() ?: 0f)
                        }
                        val dataSet = BarDataSet(entries, "Weekly sales").apply {
                            color = Color.Gray.toArgb()
                            valueTextSize = 16f
                            valueTextColor = Color.Black.toArgb()
                        }
                        chart.data = BarData(dataSet).apply { barWidth = 0.35f }
                    }
                )
            }
            Text(
                text = "$sevenDaysAgo al $now",
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 15.dp, end = 15.dp)
            )
        }
        selectedDay?.let { sale ->
            barEntryOffset?.let { position ->
                Popup(
                    offset = position
                ) {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .width(popUpInnerBoxWidth.dp)
                            .shadow(elevation = 5.dp, shape = bubbleShape())
                            .background(color = Color.White, shape = bubbleShape())
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = sale.dayDate.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd MMM",
                                        Locale.getDefault()
                                    )
                                ),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "Ganancias del dia",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "$${sale.dailyRevenue}",
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sale.dailyProductCount.toString(),
                                    textAlign = TextAlign.End,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Filled.Fastfood,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(23.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}