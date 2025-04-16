package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.data.network.model.ProductSearchNetwork
import com.bmc.buenacocinavendors.domain.model.ProductSearchDomain
import java.time.Instant
import java.time.ZoneId

object ProductSearchMapper {
    fun asDomain(network: ProductSearchNetwork): ProductSearchDomain {
        return ProductSearchDomain(
            id = network.id,
            name = network.name,
            category = ProductSearchDomain.ProductSearchCategoryDomain(
                name = network.category.name
            ),
            description = network.description,
            discount = ProductSearchDomain.ProductSearchDiscountDomain(
                percentage = network.discount.percentage.toBigDecimal(),
                startDate = Instant.ofEpochMilli(network.discount.startDate.toLong())
                    .atZone(ZoneId.of("UTC")).toLocalDateTime(),
                endDate = Instant.ofEpochMilli(network.discount.endDate.toLong())
                    .atZone(ZoneId.of("UTC")).toLocalDateTime()
            ),
            rating = network.rating.toFloat(),
            totalReviews = network.totalReviews.toBigInteger(),
            store = ProductSearchDomain.ProductSearchStoreDomain(
                name = network.store.name,
                ownerId = network.store.ownerId
            ),
            type = network.type,
            image = network.image
        )
    }
}

fun ProductSearchNetwork.asDomain(): ProductSearchDomain = ProductSearchMapper.asDomain(this)