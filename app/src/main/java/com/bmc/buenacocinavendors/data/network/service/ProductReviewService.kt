package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.PRODUCT_REVIEW_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.model.ProductReviewNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductReviewService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun get(id: String): Flow<ProductReviewNetwork?> = callbackFlow {
        val docRef = firestore.collection(PRODUCT_REVIEW_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val data = snapshot.toObject(ProductReviewNetwork::class.java)
                trySend(data)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<ProductReviewNetwork>> = callbackFlow {
        val ref = firestore.collection(PRODUCT_REVIEW_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val data = snapshot.toObjects(ProductReviewNetwork::class.java)
                trySend(data)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}