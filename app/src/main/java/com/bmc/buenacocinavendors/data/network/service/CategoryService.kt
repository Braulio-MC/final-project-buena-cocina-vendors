package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.CATEGORY_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateCategoryDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateCategoryDto
import com.bmc.buenacocinavendors.data.network.model.CategoryNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CategoryService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    fun create(
        dto: CreateCategoryDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(CATEGORY_COLLECTION_NAME).document()
        val new = hashMapOf(
            "id" to docRef.id,
            "name" to dto.name,
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
        dto: UpdateCategoryDto,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val update = hashMapOf<String, Any>(
            "id" to id,
            "currentName" to dto.currentName,
            "name" to dto.name,
            "storeId" to dto.storeId
        )
        functions
            .getHttpsCallable("category-update")
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
                        val message = exception.message ?: "Category update error"
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
        name: String,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val delete = hashMapOf(
            "id" to id,
            "name" to name
        )
        functions
            .getHttpsCallable("category-remove")
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
                        val message = exception.message ?: "Category delete error"
                        val details = exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure("Unexpected error", "Unknown error")
                    }
                }
            }
    }

    fun get(id: String): Flow<CategoryNetwork?> = callbackFlow {
        val docRef = firestore.collection(CATEGORY_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val category = snapshot.toObject(CategoryNetwork::class.java)
                trySend(category)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<CategoryNetwork>> = callbackFlow {
        val ref = firestore.collection(CATEGORY_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val categories = snapshot.toObjects(CategoryNetwork::class.java)
                trySend(categories)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}