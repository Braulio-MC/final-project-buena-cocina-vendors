package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.DISCOUNT_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateDiscountDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateDiscountDto
import com.bmc.buenacocinavendors.data.network.model.DiscountNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DiscountService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    fun create(
        dto: CreateDiscountDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(DISCOUNT_COLLECTION_NAME).document()
        val new = hashMapOf(
            "id" to docRef.id,
            "name" to dto.name,
            "percentage" to dto.percentage,
            "startDate" to dto.startDate,
            "endDate" to dto.endDate,
            "storeId" to dto.storeId,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(new)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun update(
        id: String,
        dto: UpdateDiscountDto,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val startDateMillis = dto.startDate.toDate().time
        val endDateMillis = dto.endDate.toDate().time
        val update = hashMapOf(
            "id" to id,
            "name" to dto.name,
            "percentage" to dto.percentage,
            "startDate" to startDateMillis,
            "endDate" to endDateMillis,
            "storeId" to dto.storeId
        )
        functions
            .getHttpsCallable("discount-update")
            .call(update)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as? Map<*, *>
                    when {
                        result == null -> {
                            onFailure("Unknown error", "Server did not return a response")
                        }

                        else -> {
                            val message = result["message"] as? String ?: "Successfully updated"
                            val response = result["data"] as? Map<*, *>
                            val affectedProducts = response?.get("affectedProducts") as? Int ?: 0
                            onSuccess(message, affectedProducts)
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message ?: "Discount update error"
                        val details = exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure("Unexpected error", "Unknown error")
                    }
                }
            }
    }

    fun delete(
        id: String,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val delete = hashMapOf(
            "id" to id
        )
        functions
            .getHttpsCallable("discount-remove")
            .call(delete)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as? Map<*, *>
                    when {
                        result == null -> {
                            onFailure("Unknown error", "Server did not return a response")
                        }

                        else -> {
                            val message = result["message"] as? String ?: "Successfully deleted"
                            val response = result["data"] as? Map<*, *>
                            val affectedProducts = response?.get("affectedProducts") as? Int ?: 0
                            onSuccess(message, affectedProducts)
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message ?: "Discount delete error"
                        val details = exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure("Unexpected error", "Unknown error")
                    }
                }
            }
    }

    fun get(id: String): Flow<DiscountNetwork?> = callbackFlow {
        val docRef = firestore.collection(DISCOUNT_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val discount = snapshot.toObject(DiscountNetwork::class.java)
                trySend(discount)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<DiscountNetwork>> = callbackFlow {
        val ref = firestore.collection(DISCOUNT_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val discounts = snapshot.toObjects(DiscountNetwork::class.java)
                trySend(discounts)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}