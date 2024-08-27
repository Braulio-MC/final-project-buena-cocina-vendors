package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.ORDER_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.UpdateOrderDto
import com.bmc.buenacocinavendors.data.network.model.OrderNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OrderService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun update(
        id: String,
        dto: UpdateOrderDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(ORDER_COLLECTION_NAME).document(id)
        val update = hashMapOf(
            "status" to dto.status,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.update(update)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun get(id: String): Flow<OrderNetwork?> = callbackFlow {
        val docRef = firestore.collection(ORDER_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val order = snapshot.toObject(OrderNetwork::class.java)
                trySend(order)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<OrderNetwork>> = callbackFlow {
        val ref = firestore.collection(ORDER_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val orders = snapshot.toObjects(OrderNetwork::class.java)
                trySend(orders)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}