package com.bmc.buenacocinavendors.data.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class CategoryNetwork(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val parent: CategoryParentNetwork = CategoryParentNetwork(),
    val storeId: String = "",
    val paginationKey: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    data class CategoryParentNetwork(
        val id: String = "",
        val name: String = ""
    )
}
