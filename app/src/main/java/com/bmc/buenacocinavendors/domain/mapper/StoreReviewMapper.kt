package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.StoreReviewNetwork
import com.bmc.buenacocinavendors.domain.model.StoreReviewDomain

object StoreReviewMapper {
    fun asDomain(network: StoreReviewNetwork): StoreReviewDomain {
        return StoreReviewDomain(
            id = network.documentId,
            userId = network.userId,
            storeId = network.storeId,
            rating = network.rating,
            comment = network.comment,
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun StoreReviewNetwork.asDomain(): StoreReviewDomain = StoreReviewMapper.asDomain(this)