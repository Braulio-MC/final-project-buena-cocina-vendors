package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.CategoryNetwork
import com.bmc.buenacocinavendors.domain.model.CategoryDomain

object CategoryMapper {
    fun asDomain(network: CategoryNetwork): CategoryDomain {
        return CategoryDomain(
            id = network.documentId,
            name = network.name,
            parent = CategoryDomain.CategoryParentDomain(
                id = network.parent.id,
                name = network.parent.name
            ),
            storeId = network.storeId,
            createdAt =  DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }
}

fun CategoryNetwork.asDomain() = CategoryMapper.asDomain(this)