package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.OrderStatus
import com.bmc.buenacocinavendors.domain.model.NotificationDomain
import com.bmc.buenacocinavendors.domain.repository.MessagingRepository
import javax.inject.Inject

class SendOrderStatusNotificationToSpecificUserDevices @Inject constructor(
    private val messagingRepository: MessagingRepository
) {
    operator fun invoke(
        userId: String,
        storeName: String,
        orderStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val status = OrderStatus.entries.find { it.status == orderStatus }
        status?.let { s ->
            val notification = buildNotification(storeName, s)
            messagingRepository.sendMessageToUserDevices(userId, notification, onSuccess, onFailure)
        }
    }

    private fun buildNotification(storeName: String, orderStatus: OrderStatus): NotificationDomain {
        val message = when (orderStatus) {
            OrderStatus.CREATED -> "La orden fue creada"
            OrderStatus.ACTIVE -> "La orden cambio su estado a activa, la tienda tiene en cuenta tu pedido"
            OrderStatus.PROGRESS -> "La orden cambio su estado a en progreso, la tienda esta trabajando en tu pedido"
            OrderStatus.ON_WAY -> "La orden cambio su estado a en camino, asegurate de estar en la ubicacion que elegiste para entregarte el pedido"
            OrderStatus.DELIVERED -> "Tu orden fue entregada, no olvides calificar la tienda y los productos"
            else -> ""
        }
        return NotificationDomain(
            notification = NotificationDomain.InnerNotificationDomain(
                title = "Actualización de tu pedido en $storeName",
                body = message
            ),
            data = hashMapOf()
        )
    }
}