package com.bmc.buenacocinavendors.data.network.service

import android.util.Log
import com.bmc.buenacocinavendors.core.USER_COLLECTION_NAME
import com.bmc.buenacocinavendors.core.USER_SUB_COLLECTION_TOKEN
import com.bmc.buenacocinavendors.domain.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
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
    suspend fun create(
        storeId: String = "",
        token: String? = null,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
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
                                this.exists(
                                    storeId = store.documentId,
                                    token = tokenListener,
                                    onSuccess = { exists ->
                                        if (!exists) {
                                            val fParams = hashMapOf(
                                                "userId" to store.documentId,
                                                "token" to tokenListener
                                            )
                                            functions
                                                .getHttpsCallable("pushNotification-create")
                                                .call(fParams)
                                                .addOnSuccessListener { response ->
                                                    onSuccess(response.getData())
                                                }
                                                .addOnFailureListener { e ->
                                                    onFailure(e)
                                                }
                                        }
                                    },
                                    onFailure = onFailure
                                )
                            }.addOnFailureListener { e ->
                                onFailure(e)
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
                                        functions
                                            .getHttpsCallable("pushNotification-create")
                                            .call(fParams)
                                            .addOnSuccessListener { response ->
                                                onSuccess(response.getData())
                                            }
                                            .addOnFailureListener { e ->
                                                onFailure(e)
                                            }
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
                                functions
                                    .getHttpsCallable("pushNotification-create")
                                    .call(fParams)
                                    .addOnSuccessListener { response ->
                                        onSuccess(response.getData())
                                    }
                                    .addOnFailureListener { e ->
                                        onFailure(e)
                                    }
                            }
                        },
                        onFailure = onFailure
                    )
                }.addOnFailureListener { e ->
                    onFailure(e)
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
                            functions
                                .getHttpsCallable("pushNotification-create")
                                .call(fParams)
                                .addOnSuccessListener { response ->
                                    onSuccess(response.getData())
                                }
                                .addOnFailureListener { e ->
                                    onFailure(e)
                                }
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
        onFailure: (Exception) -> Unit
    ) {
        val q = firestore.collection(USER_COLLECTION_NAME)
            .document(storeId)
            .collection(USER_SUB_COLLECTION_TOKEN)
            .whereEqualTo("token", token)
            .limit(1)
            .get()
        q.addOnSuccessListener {
            onSuccess(it.size() > 0)
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    suspend fun remove(
        storeId: String = "",
        token: String? = null,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
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
                                functions
                                    .getHttpsCallable("pushNotification-remove")
                                    .call(fParams)
                                    .addOnSuccessListener { response ->
                                        onSuccess(response.getData())
                                    }
                                    .addOnFailureListener { e ->
                                        onFailure(e)
                                    }
                            }.addOnFailureListener { e ->
                                onFailure(e)
                            }
                        } else {
                            val fParams = hashMapOf(
                                "userId" to store.documentId,
                                "token" to token
                            )
                            functions
                                .getHttpsCallable("pushNotification-remove")
                                .call(fParams)
                                .addOnSuccessListener { response ->
                                    onSuccess(response.getData())
                                }
                                .addOnFailureListener { e ->
                                    onFailure(e)
                                }
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
                    functions
                        .getHttpsCallable("pushNotification-remove")
                        .call(fParams)
                        .addOnSuccessListener { response ->
                            onSuccess(response.getData())
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }.addOnFailureListener { e ->
                    onFailure(e)
                }
            } else {
                val fParams = hashMapOf(
                    "userId" to storeId,
                    "token" to token
                )
                functions
                    .getHttpsCallable("pushNotification-remove")
                    .call(fParams)
                    .addOnSuccessListener { response ->
                        onSuccess(response.getData())
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }
        }
    }
}