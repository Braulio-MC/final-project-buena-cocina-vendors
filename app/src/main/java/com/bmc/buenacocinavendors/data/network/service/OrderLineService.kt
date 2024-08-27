package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.ORDER_COLLECTION_NAME
import com.bmc.buenacocinavendors.core.ORDER_SUB_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.model.OrderLineNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OrderLineService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun get(orderId: String, orderLineId: String): Flow<OrderLineNetwork?> = callbackFlow {
        val docRef = firestore
            .collection(ORDER_COLLECTION_NAME)
            .document(orderId)
            .collection(ORDER_SUB_COLLECTION_NAME)
            .document(orderLineId)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val orderLine = snapshot.toObject(OrderLineNetwork::class.java)
                trySend(orderLine)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(orderId: String): Flow<List<OrderLineNetwork>> = callbackFlow {
        val ref = firestore
            .collection(ORDER_COLLECTION_NAME)
            .document(orderId)
            .collection(ORDER_SUB_COLLECTION_NAME)
        val listener = ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val orderLines = snapshot.toObjects(OrderLineNetwork::class.java)
                trySend(orderLines)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove()  }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<OrderLineNetwork>> = callbackFlow {
        val ref = firestore.collectionGroup(ORDER_SUB_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val orderLines = snapshot.toObjects(OrderLineNetwork::class.java)
                trySend(orderLines)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}