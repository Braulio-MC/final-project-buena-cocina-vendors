package com.bmc.buenacocinavendors.ui.screen.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.core.getOrderStatusColor
import com.bmc.buenacocinavendors.domain.model.OrderDomain
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import java.time.LocalDateTime

@Composable
fun OrderItem(
    order: OrderDomain,
    onClick: (String) -> Unit,
) {
    val createdAt = order.createdAt?.let {
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
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(130.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.order_item),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Text(
                    text = "Informacion",
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Comprador",
                        textAlign = TextAlign.End,
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
                        text = order.user.name,
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
                        text = "Direccion",
                        textAlign = TextAlign.End,
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
                        text = order.deliveryLocation.name,
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
                        text = "Creado",
                        textAlign = TextAlign.End,
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
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(id = getOrderStatusColor(order.status))
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(IntrinsicSize.Max)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = order.status,
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Button(
                        onClick = { onClick(order.id) },
                        modifier = Modifier
                            .minimumInteractiveComponentSize(),
                        shape = RoundedCornerShape(6.dp),
                    ) {
                        Text(
                            text = "Detalles",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OrderItemPreview() {
    BuenaCocinaVendorsTheme {
        OrderItem(
            order = OrderDomain(
                id = "oWqou7mmtNdLY0aT3Z7B",
                status = OrderStatus.CREATED.status,
                isRated = false,
                user = OrderDomain.OrderUserDomain("1", "Braulio"),
                deliveryLocation = OrderDomain.OrderDeliveryLocationDomain(
                    "1",
                    "Enfrente del Chedraui"
                ),
                store = OrderDomain.OrderStoreDomain("1", ownerId = "1", "La Cuchara Verde"),
                paymentMethod = OrderDomain.OrderPaymentMethodDomain("1", "Tarjeta de credito"),
//                orderLines = listOf(
//                    OrderLineDomain(
//                        id = "1",
//                        total = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        quantity = BigInteger("4"),
//                        product = OrderLineDomain.OrderLineProductDomain(
//                            id = "1",
//                            name = "Hamburguesa de res con papas",
//                            image = "https://firebasestorage.googleapis.com/v0/b/buena-cocina-fp.appspot.com/o/images%2FLa%20cuchara%20verde%2FHamburguesa%20de%20res%20con%20papas.jpg?alt=media&token=9b1aaf42-466e-407d-a531-0f4b007b1b3e",
//                            price = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        ),
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = LocalDateTime.now(),
//                    ),
//                    OrderLineDomain(
//                        id = "1",
//                        total = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        quantity = BigInteger("4"),
//                        product = OrderLineDomain.OrderLineProductDomain(
//                            id = "1",
//                            name = "Ensalada griega",
//                            image = "https://firebasestorage.googleapis.com/v0/b/buena-cocina-fp.appspot.com/o/images%2FVegan%2FEnsalada%20griega.jpeg?alt=media&token=218d3c1d-ea30-4c08-9bc8-03250450d811",
//                            price = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        ),
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = LocalDateTime.now(),
//                    ),
//                    OrderLineDomain(
//                        id = "1",
//                        total = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        quantity = BigInteger("4"),
//                        product = OrderLineDomain.OrderLineProductDomain(
//                            id = "1",
//                            name = "Hot dog",
//                            image = "https://firebasestorage.googleapis.com/v0/b/buena-cocina-fp.appspot.com/o/images%2FLa%20cuchara%20verde%2FHot%20dog.jpg?alt=media&token=04c57da9-20d5-4bb1-a499-677be3cc4401",
//                            price = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        ),
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = LocalDateTime.now(),
//                    ),
//                    OrderLineDomain(
//                        id = "1",
//                        total = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        quantity = BigInteger("4"),
//                        product = OrderLineDomain.OrderLineProductDomain(
//                            id = "1",
//                            name = "Ensalada griega",
//                            image = "https://firebasestorage.googleapis.com/v0/b/buena-cocina-fp.appspot.com/o/images%2FVegan%2FEnsalada%20griega.jpeg?alt=media&token=218d3c1d-ea30-4c08-9bc8-03250450d811",
//                            price = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        ),
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = LocalDateTime.now(),
//                    ),
//                    OrderLineDomain(
//                        id = "1",
//                        total = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        quantity = BigInteger("4"),
//                        product = OrderLineDomain.OrderLineProductDomain(
//                            id = "1",
//                            name = "Hot dog",
//                            image = "https://firebasestorage.googleapis.com/v0/b/buena-cocina-fp.appspot.com/o/images%2FLa%20cuchara%20verde%2FHot%20dog.jpg?alt=media&token=04c57da9-20d5-4bb1-a499-677be3cc4401",
//                            price = BigDecimal(430.55).setScale(2, RoundingMode.HALF_UP),
//                        ),
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = LocalDateTime.now(),
//                    ),
//                ),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            onClick = {}
        )
    }
}


