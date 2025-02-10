package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.StoreNetwork
import com.bmc.buenacocinavendors.domain.model.StoreDomain

object StoreMapper {
    fun asDomain(network: StoreNetwork): StoreDomain {
        return StoreDomain(
            id = network.documentId,
            name = network.name,
            description = network.description,
            email = network.email,
            phoneNumber = network.phoneNumber,
            rating = network.rating.toBigDecimal(),
            totalRating = network.totalRating.toBigDecimal(),
            totalReviews = network.totalReviews.toBigInteger(),
            image = network.image,
            userId = network.userId,
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun StoreNetwork.asDomain(): StoreDomain = StoreMapper.asDomain(this)