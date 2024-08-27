package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.DiscountNetwork
import com.bmc.buenacocinavendors.domain.model.DiscountDomain

object DiscountMapper {
    fun asDomain(network: DiscountNetwork): DiscountDomain {
        return DiscountDomain(
            id = network.documentId,
            name = network.name,
            percentage = network.percentage.toBigDecimal(),
            startDate = DateUtils.firebaseTimestampToLocalDateTime(network.startDate),
            endDate = DateUtils.firebaseTimestampToLocalDateTime(network.endDate),
            storeId = network.storeId,
            createdAt = DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun DiscountNetwork.asDomain() = DiscountMapper.asDomain(this)