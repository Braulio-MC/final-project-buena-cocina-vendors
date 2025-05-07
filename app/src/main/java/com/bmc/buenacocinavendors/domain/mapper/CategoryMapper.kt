package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.data.network.model.CategoryNetwork
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.domain.model.ProductDomain

object CategoryMapper {
    fun asDomain(network: CategoryNetwork): CategoryDomain {
        return CategoryDomain(
            id = network.documentId,
            name = network.name,
            storeId = network.storeId,
            createdAt =  DateUtils.firebaseTimestampToLocalDateTime(network.createdAt),
            updatedAt = DateUtils.firebaseTimestampToLocalDateTime(network.updatedAt)
        )
    }

    fun asProductCategoryDomain(domain: CategoryDomain): ProductDomain.ProductCategoryDomain {
        return ProductDomain.ProductCategoryDomain(
            id = domain.id,
            name = domain.name,
        )
    }
}

fun CategoryNetwork.asDomain() = CategoryMapper.asDomain(this)
fun CategoryDomain.asProductCategoryDomain() = CategoryMapper.asProductCategoryDomain(this)