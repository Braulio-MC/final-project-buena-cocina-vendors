package com.bmc.buenacocinavendors.ui.screen.order.detailed.rating

import com.bmc.buenacocinavendors.domain.model.OrderLineDomain
import com.bmc.buenacocinavendors.domain.model.ProductReviewDomain
import com.bmc.buenacocinavendors.domain.model.StoreReviewDomain

data class DetailedOrderRatingUiState(
    val isLoading: Boolean = false,
    val storeRating: StoreReviewDomain? = null,
    val orderLines: List<OrderLineDomain> = emptyList(),
    val itemRatings: List<ProductReviewDomain> = emptyList()
)
