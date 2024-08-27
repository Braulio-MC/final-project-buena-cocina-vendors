package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.CATEGORY_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateCategoryDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateCategoryDto
import com.bmc.buenacocinavendors.data.network.model.CategoryNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
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
            "parent" to hashMapOf(
                "id" to dto.parent.id,
                "name" to dto.parent.name
            ),
            "storeId" to dto.storeId,
            "paginationKey" to UUID.randomUUID().toString(),
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
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val update = hashMapOf<String, Any>(
            "id" to id,
            "name" to dto.name,
            "parentId" to dto.parent.id,
            "parentName" to dto.parent.name,
            "storeId" to dto.storeId
        )
        functions
            .getHttpsCallable("category-update")
            .call(update)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun delete(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val delete = hashMapOf(
            "id" to id
        )
        functions
            .getHttpsCallable("category-remove")
            .call(delete)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
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