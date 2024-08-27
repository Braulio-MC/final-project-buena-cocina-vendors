package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.bmc.buenacocinavendors.domain.repository.StoreRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.error.AuthError
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.ui.navigation.NavigationState
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val auth0Manager: SecureCredentialsManager,
) : ViewModel() {
    suspend fun checkNavigationState(): NavigationState {
        if (!auth0Manager.hasValidCredentials()) {
            return NavigationState.NotAuthenticated
        }

        when (val resultUser = userRepository.getUserId()) {
            is Result.Error -> {
                when (resultUser.error) {
                    AuthError.NOT_AUTHENTICATED -> {
                        return NavigationState.NotAuthenticated
                    }

                    AuthError.NOT_VALID_USER_ID -> {

                    }
                }
            }

            is Result.Success -> {
                val qStore: (Query) -> Query = { query ->
                    query.whereEqualTo("userId", resultUser.data)
                }
                val store = storeRepository.get(qStore).firstOrNull()?.firstOrNull()
                return if (store == null) {
                    NavigationState.NotStore
                } else {
                    NavigationState.HasStore(store.id)
                }
            }
        }
        return NavigationState.Loading
    }
}