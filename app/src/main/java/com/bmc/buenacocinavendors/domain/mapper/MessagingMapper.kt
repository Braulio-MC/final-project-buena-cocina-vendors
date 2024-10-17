package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.data.network.dto.CreateNotificationDto
import com.bmc.buenacocinavendors.domain.model.NotificationDomain

object MessagingMapper {
    fun asNetwork(domain: NotificationDomain): CreateNotificationDto {
        return CreateNotificationDto(
            notification = CreateNotificationDto.CreateInnerNotificationDto(
                title = domain.notification.title,
                body = domain.notification.body
            ),
            data = domain.data
        )
    }

    fun asDomain(network: CreateNotificationDto): NotificationDomain {
        return NotificationDomain(
            notification = NotificationDomain.InnerNotificationDomain(
                title = network.notification.title,
                body = network.notification.body
            ),
            data = network.data
        )
    }
}

fun NotificationDomain.asNetwork() = MessagingMapper.asNetwork(this)
fun CreateNotificationDto.asDomain() = MessagingMapper.asDomain(this)