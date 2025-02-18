package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.ProductNetwork
import com.bmc.buenacocinavendors.domain.model.ProductDomain

object ProductMapper {
    fun asDomain(network: ProductNetwork): ProductDomain {
        return ProductDomain(
            id = network.documentId,
            name = network.name,
            description = network.description,
            image = network.image,
            price = network.price.toBigDecimal(),
            quantity = network.quantity.toBigInteger(),
            discount = ProductDomain.ProductDiscountDomain(
                id = network.discount.id,
                percentage = network.discount.percentage.toBigDecimal(),
                startDate = DateUtils.firebaseTimestampToLocalDateTime(network.discount.startDate),
                endDate = DateUtils.firebaseTimestampToLocalDateTime(network.discount.endDate)
            ),
            store = ProductDomain.ProductStoreDomain(
                id = network.store.id,
                name = network.store.name,
                ownerId = network.store.ownerId
            ),
            category = ProductDomain.ProductCategoryDomain(
                id = network.category.id,
                name = network.category.name,
                parentName = network.category.parentName
            ),
            rating = network.rating.toBigDecimal(),
            totalRating = network.totalRating.toBigDecimal(),
            totalReviews = network.totalReviews.toBigInteger(),
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun ProductNetwork.asDomain(): ProductDomain = ProductMapper.asDomain(this)