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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
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
        onFailure: (Exception) -> Unit
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
            .addOnSuccessListener { result ->
                val new = hashMapOf(
                    "id" to docRef.id,
                    "name" to dto.name,
                    "description" to dto.description,
                    "price" to dto.price,
                    "image" to result.data,
                    "quantity" to dto.quantity,
                    "category" to hashMapOf(
                        "id" to dto.category.id,
                        "name" to dto.category.name,
                        "parentName" to dto.category.parentName
                    ),
                    "store" to hashMapOf(
                        "id" to dto.store.id,
                        "name" to dto.store.name
                    ),
                    "discount" to hashMapOf(
                        "id" to dto.discount.id,
                        "percentage" to dto.discount.percentage,
                        "startDate" to dto.discount.startDate,
                        "endDate" to dto.discount.endDate
                    ),
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
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun update(
        id: String,
        dto: UpdateProductDto,
        imageBase64: String?,
        imageName: String?,
        imageType: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val startDateMillis = dto.discount.startDate.toDate().time
        val endDateMillis = dto.discount.endDate.toDate().time
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
                .addOnSuccessListener { result ->
                    val update = hashMapOf(
                        "id" to id,
                        "name" to dto.name,
                        "description" to dto.description,
                        "price" to dto.price,
                        "image" to result.data,
                        "quantity" to dto.quantity,
                        "categoryId" to dto.category.id,
                        "categoryName" to dto.category.name,
                        "categoryParentName" to dto.category.parentName,
                        "storeId" to dto.store.id,
                        "storeName" to dto.store.name,
                        "usingDefaultDiscount" to dto.discount.useDefault,
                        "discountId" to dto.discount.id,
                        "discountPercentage" to dto.discount.percentage,
                        "discountStartDate" to startDateMillis,
                        "discountEndDate" to endDateMillis,
                    )
                    functions
                        .getHttpsCallable("product-update")
                        .call(update)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)

                        }
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        } else {
            val update = hashMapOf(
                "id" to id,
                "name" to dto.name,
                "description" to dto.description,
                "price" to dto.price,
                "image" to "",
                "quantity" to dto.quantity,
                "categoryId" to dto.category.id,
                "categoryName" to dto.category.name,
                "categoryParentName" to dto.category.parentName,
                "storeId" to dto.store.id,
                "storeName" to dto.store.name,
                "usingDefaultDiscount" to dto.discount.useDefault,
                "discountId" to dto.discount.id,
                "discountPercentage" to dto.discount.percentage,
                "discountStartDate" to startDateMillis,
                "discountEndDate" to endDateMillis,
            )
            functions
                .getHttpsCallable("product-update")
                .call(update)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
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
            .getHttpsCallable("product-remove")
            .call(delete)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
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