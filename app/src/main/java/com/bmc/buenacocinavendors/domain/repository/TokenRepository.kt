package com.bmc.buenacocinavendors.domain.repository

import android.util.Log
import com.bmc.buenacocinavendors.core.USER_COLLECTION_NAME
import com.bmc.buenacocinavendors.core.USER_SUB_COLLECTION_TOKEN
import com.bmc.buenacocinavendors.domain.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    suspend fun create(
        storeId: String = "",
        token: String,
        onSuccess: (Any?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (storeId.isEmpty()) {
            when (val result = userRepository.getUserId()) {
                is Result.Error -> {
                    Log.d("TokenRepository", result.error.toString())
                }

                is Result.Success -> {
                    val qStore: (Query) -> Query = { query ->
                        query.whereEqualTo("userId", result.data)
                    }
                    val store = storeRepository.get(qStore).firstOrNull()?.firstOrNull()
                    if (store != null) {
                        val fParams = hashMapOf(
                            "userId" to store.id,
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
                }
            }
        } else {
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
    }

    fun exists(
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
}