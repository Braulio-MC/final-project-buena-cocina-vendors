package com.bmc.buenacocinavendors.data.network.service

import android.util.Log
import com.bmc.buenacocinavendors.core.USER_COLLECTION_NAME
import com.bmc.buenacocinavendors.core.USER_SUB_COLLECTION_TOKEN
import com.bmc.buenacocinavendors.domain.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenService @Inject constructor(
    private val storeService: StoreService,
    private val userService: UserService,
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions,
    private val messaging: FirebaseMessaging
) {
    private fun callPushNotificationCreate(
        fParams: HashMap<String, String>,
        onSuccess: (String) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        functions
            .getHttpsCallable("pushNotification-create")
            .call(fParams)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as? Map<*, *>
                    when {
                        result == null -> {
                            onFailure(
                                "Unknown error",
                                "Server did not return a response"
                            )
                        }

                        else -> {
                            val message = result["message"] as? String ?: ""
                            onSuccess(message)
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message
                            ?: "Push notification token create error"
                        val details =
                            exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure(
                            "Unexpected error",
                            "Unknown error"
                        )
                    }
                }
            }
    }

    private fun callPushNotificationRemove(
        fParams: HashMap<String, String>,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        functions
            .getHttpsCallable("pushNotification-remove")
            .call(fParams)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result?.data as? Map<*, *>
                    when {
                        result == null -> {
                            onFailure(
                                "Unknown error",
                                "Server did not return a response"
                            )
                        }

                        else -> {
                            val message = result["message"] as? String ?: ""
                            val response = result["data"] as? Map<*, *>
                            val processedCount = response?.get("processedCount") as? Int ?: 0
                            onSuccess(message, processedCount)
                        }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseFunctionsException) {
                        val message = exception.message
                            ?: "Push notification token remove error"
                        val details =
                            exception.details?.toString() ?: ""
                        onFailure(message, details)
                    } else {
                        onFailure(
                            "Unexpected error",
                            "Unknown error"
                        )
                    }
                }
            }
    }

    suspend fun create(
        storeId: String = "",
        token: String? = null,
        onSuccess: (String) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        if (storeId.isEmpty()) {
            when (val userIdResult = userService.getUserId()) {
                is Result.Error -> {
                    Log.d("TokenService", userIdResult.error.toString())
                }

                is Result.Success -> {
                    val qStore: (Query) -> Query = { query ->
                        query.whereEqualTo("userId", userIdResult.data)
                    }
                    val store = storeService.get(qStore).firstOrNull()?.firstOrNull()
                    if (store != null) {
                        if (token == null) {
                            messaging.token.addOnSuccessListener { tokenListener ->
                                this.exists(
                                    storeId = store.documentId,
                                    token = tokenListener,
                                    onSuccess = { exists ->
                                        if (!exists) {
                                            val fParams = hashMapOf(
                                                "userId" to store.documentId,
                                                "token" to tokenListener
                                            )
                                            callPushNotificationCreate(
                                                fParams = fParams,
                                                onSuccess = onSuccess,
                                                onFailure = onFailure
                                            )
                                        }
                                    },
                                    onFailure = onFailure
                                )
                            }.addOnFailureListener { _ ->
                                onFailure(
                                    "Unexpected error",
                                    "An error occurred while getting the token"
                                )
                            }
                        } else {
                            this.exists(
                                storeId = store.documentId,
                                token = token,
                                onSuccess = { exists ->
                                    if (!exists) {
                                        val fParams = hashMapOf(
                                            "userId" to store.documentId,
                                            "token" to token
                                        )
                                        callPushNotificationCreate(
                                            fParams = fParams,
                                            onSuccess = onSuccess,
                                            onFailure = onFailure
                                        )
                                    }
                                },
                                onFailure = onFailure
                            )
                        }
                    }
                }
            }
        } else {
            if (token == null) {
                messaging.token.addOnSuccessListener { tokenListener ->
                    this.exists(
                        storeId = storeId,
                        token = tokenListener,
                        onSuccess = { exists ->
                            if (!exists) {
                                val fParams = hashMapOf(
                                    "userId" to storeId,
                                    "token" to tokenListener
                                )
                                callPushNotificationCreate(
                                    fParams = fParams,
                                    onSuccess = onSuccess,
                                    onFailure = onFailure
                                )
                            }
                        },
                        onFailure = onFailure
                    )
                }.addOnFailureListener { _ ->
                    onFailure(
                        "Unexpected error",
                        "An error occurred while getting the token"
                    )
                }
            } else {
                this.exists(
                    storeId = storeId,
                    token = token,
                    onSuccess = { exists ->
                        if (!exists) {
                            val fParams = hashMapOf(
                                "userId" to storeId,
                                "token" to token
                            )
                            callPushNotificationCreate(
                                fParams = fParams,
                                onSuccess = onSuccess,
                                onFailure = onFailure
                            )
                        }
                    },
                    onFailure = onFailure
                )
            }
        }
    }

    private fun exists(
        storeId: String,
        token: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        val q = firestore.collection(USER_COLLECTION_NAME)
            .document(storeId)
            .collection(USER_SUB_COLLECTION_TOKEN)
            .whereEqualTo("token", token)
            .limit(1)
            .get()
        q.addOnSuccessListener {
            onSuccess(it.size() > 0)
        }.addOnFailureListener { _ ->
            onFailure("Unexpected error", "An error occurred while checking if token exists")
        }
    }

    suspend fun remove(
        storeId: String = "",
        token: String? = null,
        onSuccess: (String, Int) -> Unit,
        onFailure: (String, String) -> Unit
    ) {
        if (storeId.isEmpty()) {
            when (val result = userService.getUserId()) {
                is Result.Error -> {
                    Log.d("TokenService", result.error.toString())
                }

                is Result.Success -> {
                    val qStore: (Query) -> Query = { query ->
                        query.whereEqualTo("userId", result.data)
                    }
                    val store = storeService.get(qStore).firstOrNull()?.firstOrNull()
                    if (store != null) {
                        if (token == null) {
                            messaging.token.addOnSuccessListener { tokenListener ->
                                val fParams = hashMapOf(
                                    "userId" to store.documentId,
                                    "token" to tokenListener
                                )
                                callPushNotificationRemove(
                                    fParams = fParams,
                                    onSuccess = onSuccess,
                                    onFailure = onFailure
                                )
                            }.addOnFailureListener { _ ->
                                onFailure(
                                    "Unexpected error",
                                    "An error occurred while getting the token"
                                )
                            }
                        } else {
                            val fParams = hashMapOf(
                                "userId" to store.documentId,
                                "token" to token
                            )
                            callPushNotificationRemove(
                                fParams = fParams,
                                onSuccess = onSuccess,
                                onFailure = onFailure
                            )
                        }
                    }
                }
            }
        } else {
            if (token == null) {
                messaging.token.addOnSuccessListener { tokenListener ->
                    val fParams = hashMapOf(
                        "userId" to storeId,
                        "token" to tokenListener
                    )
                    callPushNotificationRemove(
                        fParams = fParams,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                }.addOnFailureListener { _ ->
                    onFailure(
                        "Unexpected error",
                        "An error occurred while getting the token"
                    )
                }
            } else {
                val fParams = hashMapOf(
                    "userId" to storeId,
                    "token" to token
                )
                callPushNotificationRemove(
                    fParams = fParams,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            }
        }
    }
}