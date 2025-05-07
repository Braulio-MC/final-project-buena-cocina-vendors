package com.bmc.buenacocinavendors.data.network.service

import com.bmc.buenacocinavendors.core.FIREBASE_STORAGE_IMAGES_ROOT_DIR
import com.bmc.buenacocinavendors.core.PRODUCT_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.dto.CreateProductDto
import com.bmc.buenacocinavendors.data.network.dto.UpdateProductDto
import com.bmc.buenacocinavendors.data.network.model.ProductNetwork
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    fun create(
        dto: CreateProductDto,
        imageBase64: String,
        imageName: String,
        imageType: String,
        onSuccess: () -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val docRef = firestore.collection(PRODUCT_COLLECTION_NAME).document()
        val path = "$FIREBASE_STORAGE_IMAGES_ROOT_DIR/${dto.store.id}"
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
                                    "price" to dto.price,
                                    "image" to imageFirebaseUrl,
                                    "quantity" to dto.quantity,
                                    "categories" to dto.categories.map {
                                        hashMapOf(
                                            "id" to it.id,
                                            "name" to it.name
                                        )
                                    },
                                    "store" to hashMapOf(
                                        "id" to dto.store.id,
                                        "name" to dto.store.name,
                                        "ownerId" to dto.store.ownerId
                                    ),
                                    "discount" to hashMapOf(
                                        "id" to dto.discount.id,
                                        "percentage" to dto.discount.percentage,
                                        "startDate" to dto.discount.startDate,
                                        "endDate" to dto.discount.endDate
                                    ),
                                    "rating" to 0.0,
                                    "totalRating" to 0.0,
                                    "totalReviews" to 0,
                                    "createdAt" to FieldValue.serverTimestamp(),
                                    "updatedAt" to FieldValue.serverTimestamp()
                                )
                                docRef.set(new)
                                    .addOnSuccessListener {
                                        onSuccess()
                                    }
                                    .addOnFailureListener { _ ->
                                        onFailure(
                                            "Product create error",
                                            "An unexpected error occurred while creating the product"
                                        )
                                    }
                            }
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message ?: "Product create error"
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
        dto: UpdateProductDto,
        imageBase64: String?,
        imageName: String?,
        imageType: String?,
        onSuccess: (String, Int, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val startDateMillis = dto.discount.startDate.toDate().time
        val endDateMillis = dto.discount.endDate.toDate().time
        var imageFirebaseUrl = ""
        if (imageBase64 != null && imageName != null && imageType != null) {
            val path = "$FIREBASE_STORAGE_IMAGES_ROOT_DIR/${dto.store.id}"
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
                                    imageFirebaseUrl = newImageUrl
                                }
                            }
                        }
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseFunctionsException) {
                            val message = exception.message ?: "Update image error"
                            val details = exception.details?.toString() ?: ""
                            onFailure(message, details)
                        } else {
                            println(exception?.message)
                            onFailure("Unexpected error", "Unknown error")
                        }
                    }
                }
        }
        val update = hashMapOf(
            "id" to id,
            "name" to dto.name,
            "description" to dto.description,
            "price" to dto.price,
            "image" to imageFirebaseUrl,
            "quantity" to dto.quantity,
            "categories" to dto.categories.map {
                hashMapOf(
                    "id" to it.id,
                    "name" to it.name
                )
            },
            "storeId" to dto.store.id,
            "storeName" to dto.store.name,
            "storeOwnerId" to dto.store.ownerId,
            "usingDefaultDiscount" to dto.discount.useDefault,
            "discountId" to dto.discount.id,
            "discountPercentage" to dto.discount.percentage,
            "discountStartDate" to startDateMillis,
            "discountEndDate" to endDateMillis
        )
        functions
            .getHttpsCallable("product-update")
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
                            val affectedProductFavorites =
                                response?.get("affectedProductFavorites") as? Int ?: 0
                            val affectedShoppingCartProducts =
                                response?.get("affectedShoppingCartProducts") as? Int ?: 0
                            onSuccess(
                                message,
                                affectedProductFavorites,
                                affectedShoppingCartProducts
                            )
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message ?: "Update product error"
                        val details = exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        println(exception?.message)
                        onFailure("Unexpected error", "Unknown error")
                    }
                }
            }
    }

    fun get(id: String): Flow<ProductNetwork?> = callbackFlow {
        val docRef = firestore.collection(PRODUCT_COLLECTION_NAME).document(id)
        val listener = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && snapshot.exists()) {
                val product = snapshot.toObject(ProductNetwork::class.java)
                trySend(product)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun get(query: (Query) -> Query = { it }): Flow<List<ProductNetwork>> = callbackFlow {
        val ref = firestore.collection(PRODUCT_COLLECTION_NAME)
        val q = query(ref)
        val listener = q.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else if (snapshot != null && !snapshot.isEmpty) {
                val products = snapshot.toObjects(ProductNetwork::class.java)
                trySend(products)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listener.remove() }
    }
}