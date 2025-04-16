package com.bmc.buenacocinavendors.domain

import com.algolia.client.model.search.Hit
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_ORDERS_INDEX
import com.bmc.buenacocinavendors.core.ALGOLIA_SEARCH_PRODUCTS_INDEX
import com.bmc.buenacocinavendors.core.Searchable
import com.bmc.buenacocinavendors.core.SearchableTypes
import com.bmc.buenacocinavendors.data.network.model.OrderSearchNetwork
import com.bmc.buenacocinavendors.data.network.model.ProductSearchNetwork
import com.bmc.buenacocinavendors.data.network.model.toOrderNetwork
import com.bmc.buenacocinavendors.data.network.model.toProductNetwork
import com.bmc.buenacocinavendors.domain.mapper.asDomain

fun Hit.toSearchable(indexName: String?): Searchable {
    return when (indexName) {
        ALGOLIA_SEARCH_PRODUCTS_INDEX -> toProductNetwork()
        ALGOLIA_SEARCH_ORDERS_INDEX -> toOrderNetwork()
        else -> throw IllegalArgumentException("Invalid index name: $indexName")
    }
}

fun Searchable.toDomain(): Searchable {
    return when (this.type) {
        SearchableTypes.PRODUCTS -> (this as ProductSearchNetwork).asDomain()
        SearchableTypes.ORDERS -> (this as OrderSearchNetwork).asDomain()
    }
}