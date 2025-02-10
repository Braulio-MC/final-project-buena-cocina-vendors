package com.bmc.buenacocinavendors.data.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class ProductNetwork(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val discount: ProductDiscountNetwork = ProductDiscountNetwork(),
    val store: ProductStoreNetwork = ProductStoreNetwork(),
    val category: ProductCategoryNetwork = ProductCategoryNetwork(),
    val rating: Double = 0.0,
    val totalRating: Double = 0.0,
    val totalReviews: Int = 0,
    val paginationKey: String = "",
    @ServerTimestamp
    val updatedAt: Timestamp? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    data class ProductDiscountNetwork (
        val id: String = "",
        val percentage: Double = 0.0,
        val startDate: Timestamp? = null,
        val endDate: Timestamp? = null
    )

    data class ProductStoreNetwork (
        val id: String = "",
        val name: String = ""
    )

    data class ProductCategoryNetwork (
        val id: String = "",
        val name: String = "",
        val parentName: String = ""
    )
}