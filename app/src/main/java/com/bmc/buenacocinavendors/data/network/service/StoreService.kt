package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.FIREBASE_STORAGE_IMAGES_ROOT_DIR
import com.bmc.buenacocinavendors.core.STORE_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateStoreDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateStoreDto
import com.bmc.buenacocinavendors.data.network.model.StoreNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class StoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    fun create(
        dto: CreateStoreDto,
        imageBase64: String,
        imageName: String,
        imageType: String,
        onSuccess: () -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val docRef = firestore.collection(STORE_COLLECTION_NAME).document()
        val path = "$FIREBASE_STORAGE_IMAGES_ROOT_DIR/${docRef.id}"
        val fParams = hashMapOf(
            "imageBase64" to imageBase64,
            "path" to path,
            "fileName" to imageName,
            "fileType" to imageType
        )
        functions
            .getHttpsCallable("uploadImage")
            .call(fParams)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as? Map<*, *>
                    when {
                        result == null -> {
                            onFailure(
                                "Unknown error",
                                "Server did not return a response performing image upload"
                            )
                        }

                        else -> {
                            val imageFirebaseUrl = result["data"] as? String ?: ""
                            if (imageFirebaseUrl.isEmpty()) {
                                onFailure(
                                    "Upload error",
                                    "Server did not return a valid image URL performing image upload"
                                )
                            } else {
                                val new = hashMapOf(
                                    "id" to docRef.id,
                                    "name" to dto.name,
                                    "description" to dto.description,
                                    "email" to dto.email,
                                    "image" to imageFirebaseUrl,
                                    "phoneNumber" to dto.phoneNumber,
                                    "startTime" to hashMapOf(
                                        "hour" to dto.startTime.hour,
                                        "minute" to dto.startTime.minute
                                    ),
                                    "endTime" to hashMapOf(
                                        "hour" to dto.endTime.hour,
                                        "minute" to dto.endTime.minute
                                    ),
                                    "rating" to 0,
                                    "totalRating" to 0,
                                    "totalReviews" to 0,
                                    "userId" to dto.userId,
                                    "createdAt" to FieldValue.serverTimestamp(),
                                    "updatedAt" to FieldValue.serverTimestamp()
                                )
                                docRef.set(new)
                                    .addOnSuccessListener {
                                        onSuccess()
                                    }
                                    .addOnFailureListener { _ ->
                                        onFailure(
                                            "Store create error",
                                            "An unexpected error occurred while creating the store"
                                        )
                                    }
                            }
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message ?: "Store create error"
                        val details = exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure("Unexpected error", "Unknown error")
                    }
                }
            }
    }

    fun update(
        id: String,
        dto: UpdateStoreDto,
        imageBase64: String?,
        imageName: String?,
        imageType: String?,
        onSuccess: () -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val docRef = firestore.collection(STORE_COLLECTION_NAME).document(id)
        if (imageBase64 != null && imageName != null && imageType != null) {
            val path = "$FIREBASE_STORAGE_IMAGES_ROOT_DIR/${docRef.id}"
            val fParams = hashMapOf(
                "oldPath" to dto.oldPath,
                "newPath" to path,
                "newImageBase64" to imageBase64,
                "newFileName" to imageName,
                "newFileType" to imageType
            )
            functions
                .getHttpsCallable("updateImage")
                .call(fParams)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result?.data as? Map<*, *>
                        when {
                            result == null -> {
                                onFailure(
                                    "Unknown error",
                                    "Server did not return a response performing image update"
                                )
                            }

                            else -> {
                                val newImageUrl = result["data"] as? String ?: ""
                                if (newImageUrl.isEmpty()) {
                                    onFailure(
                                        "Upload error",
                                        "Server did not return a valid image URL performing image update"
                                    )
                                } else {
                                    val update = hashMapOf(
                                        "name" to dto.name,
                                        "description" to dto.description,
                                        "email" to dto.email,
                                        "phoneNumber" to dto.phoneNumber,
                                        "startTime" to hashMapOf(
                                            "hour" to dto.startTime.hour,
                                            "minute" to dto.startTime.minute
                                        ),
                                        "endTime" to hashMapOf(
                                            "hour" to dto.endTime.hour,
                                            "minute" to dto.endTime.minute
                                        ),
                                        "userId" to dto.userId,
                                        "image" to newImageUrl,
                                        "updatedAt" to FieldValue.serverTimestamp()
                                    )
                                    docRef.update(update)
                                        .addOnSuccessListener {
                                            onSuccess()
                                        }
                                        .addOnFailureListener { _ ->
                                            onFailure(
                                                "Store update error",
                                                "An unexpected error occurred while updating the store"
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
        } else {
            val update = hashMapOf(
                "name" to dto.name,
                "description" to dto.description,
                "email" to dto.email,
                "phoneNumber" to dto.phoneNumber,
                "startTime" to hashMapOf(
                    "hour" to dto.startTime.hour,
                    "minute" to dto.startTime.minute
                ),
                "endTime" to hashMapOf(
                    "hour" to dto.endTime.hour,
                    "minute" to dto.endTime.minute
                ),
                "userId" to dto.userId,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.update(update)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { _ ->
                    onFailure(
                        "Store update error",
                        "An unexpected error occurred while updating the store"
                    )
                }
        }
    }

    fun get(id: String): Flow<StoreNetwork?> = callbackFlow {
        val docRef = firestore.collection(STORE_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val store = snapshot.toObject(StoreNetwork::class.java)
                trySend(store)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<StoreNetwork>> = callbackFlow {
        val ref = firestore.collection(STORE_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val stores = snapshot.toObjects(StoreNetwork::class.java)
                trySend(stores)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}