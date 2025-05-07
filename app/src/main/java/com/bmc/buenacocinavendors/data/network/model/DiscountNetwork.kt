package com.bmc.buenacocinavendors.data.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class DiscountNetwork(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val percentage: Double = 0.0,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val storeId: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)
