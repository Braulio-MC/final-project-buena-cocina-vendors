package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.STORE_REVIEW_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.model.StoreReviewNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class StoreReviewService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun get(id: String): Flow<StoreReviewNetwork?> = callbackFlow {
        val docRef = firestore.collection(STORE_REVIEW_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val review = snapshot.toObject(StoreReviewNetwork::class.java)
                trySend(review)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<StoreReviewNetwork>> = callbackFlow {
        val ref = firestore.collection(STORE_REVIEW_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val reviews = snapshot.toObjects(StoreReviewNetwork::class.java)
                trySend(reviews)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}