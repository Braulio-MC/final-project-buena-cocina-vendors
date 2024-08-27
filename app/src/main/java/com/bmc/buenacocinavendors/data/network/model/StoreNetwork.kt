package com.bmc.buenacocinavendors.data.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class StoreNetwork(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val description: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val image: String = "",
    val userId: String = "",
    val paginationKey: String = "",
    @ServerTimestamp
    val updatedAt: Timestamp? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null
)