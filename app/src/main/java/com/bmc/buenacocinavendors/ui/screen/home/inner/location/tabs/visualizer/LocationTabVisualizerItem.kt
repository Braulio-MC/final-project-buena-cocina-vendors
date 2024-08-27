package com.bmc.buenacocinavendors.ui.screen.home.inner.location.tabs.visualizer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.model.LocationDomain
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import java.time.LocalDateTime

@Composable
fun LocationTabVisualizerItem(
    location: LocationDomain,
) {
    val createdAt = location.createdAt?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"
    val updatedAt = location.updatedAt?.let {
        DateUtils.localDateTimeToString(it)
    } ?: "No se pudo obtener la fecha"

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Text(
                    text = location.name,
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Fecha de creacion",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = createdAt,
                        textAlign = TextAlign.End,
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
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Ultima actualizacion",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = updatedAt,
                        textAlign = TextAlign.End,
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
    }
}

@Composable
@Preview(showBackground = true)
fun LocationTabVisualizerItemPreview() {
    BuenaCocinaVendorsTheme {
        LocationTabVisualizerItem(
            location = LocationDomain(
                id = "1",
                name = "Enfrente del Chedraui",
                description = "En las mesas",
                storeId = "4123cr43rterfc2x45",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
    }
}