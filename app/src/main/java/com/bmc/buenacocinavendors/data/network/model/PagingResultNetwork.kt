package com.bmc.buenacocinavendors.data.network.model

data class PagingResultNetwork<T: Any>(
    val data: T,
    val pagination: PagingResultPaginationNetwork
) {
    data class PagingResultPaginationNetwork(
        val prev: String?,
        val next: String?
    )
}
