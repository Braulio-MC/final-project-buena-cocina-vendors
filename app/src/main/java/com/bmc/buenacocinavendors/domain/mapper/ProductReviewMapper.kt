package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.ProductReviewNetwork
import com.bmc.buenacocinavendors.domain.model.ProductReviewDomain

object ProductReviewMapper {
    fun asDomain(network: ProductReviewNetwork): ProductReviewDomain {
        return ProductReviewDomain(
            id = network.documentId,
            userId = network.userId,
            productId = network.productId,
            rating = network.rating,
            comment = network.comment,
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt),
        )
    }
}

fun ProductReviewNetwork.asDomain(): ProductReviewDomain = ProductReviewMapper.asDomain(this)