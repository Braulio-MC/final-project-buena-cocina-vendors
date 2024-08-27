package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.LOCATION_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateLocationDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateLocationDto
import com.bmc.buenacocinavendors.data.network.model.LocationNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class LocationService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    fun create(
        dto: CreateLocationDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = firestore.collection(LOCATION_COLLECTION_NAME).document()
        val new = hashMapOf(
            "id" to docRef.id,
            "name" to dto.name,
            "description" to dto.description,
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
        dto: UpdateLocationDto,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val update = hashMapOf(
            "id" to id,
            "name" to dto.name,
            "description" to dto.description,
            "storeId" to dto.storeId
        )
        functions
            .getHttpsCallable("location-update")
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
            .getHttpsCallable("location-remove")
            .call(delete)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun get(id: String): Flow<LocationNetwork?> = callbackFlow {
        val docRef = firestore.collection(LOCATION_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val location = snapshot.toObject(LocationNetwork::class.java)
                trySend(location)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<LocationNetwork>> = callbackFlow {
        val ref = firestore.collection(LOCATION_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val locations = snapshot.toObjects(LocationNetwork::class.java)
                trySend(locations)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}