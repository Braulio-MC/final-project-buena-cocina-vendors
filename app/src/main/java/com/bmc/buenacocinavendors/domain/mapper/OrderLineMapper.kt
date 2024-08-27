package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.OrderLineNetwork
import com.bmc.buenacocinavendors.domain.model.OrderLineDomain

object OrderLineMapper {
    fun asDomain(network: OrderLineNetwork): OrderLineDomain {
        return OrderLineDomain(
            id = network.documentId,
            quantity = network.quantity.toBigInteger(),
            product = OrderLineDomain.OrderLineProductDomain(
                id = network.product.id,
                name = network.product.name,
                description = network.product.description,
                image = network.product.image,
                price = network.product.price.toBigDecimal(),
                discount = OrderLineDomain.OrderLineProductDomain.OrderLineProductDiscountDomain(
                    id = network.product.discount.id,
                    percentage = network.product.discount.percentage.toBigDecimal(),
                    startDate = DateUtils.firebaseTimestampToLocalDateTime(network.product.discount.startDate),
                    endDate = DateUtils.firebaseTimestampToLocalDateTime(network.product.discount.endDate)
                )
            ),
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun OrderLineNetwork.asDomain(): OrderLineDomain = OrderLineMapper.asDomain(this)