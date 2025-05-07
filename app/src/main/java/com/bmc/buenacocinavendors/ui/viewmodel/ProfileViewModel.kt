package com.bmc.buenacocinavendors.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.data.preferences.PreferencesService
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.UserRepository
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.repository.ChatRepository
import com.bmc.buenacocinavendors.domain.repository.TokenRepository
import com.bmc.buenacocinavendors.ui.screen.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth0Account: Auth0,
    private val auth0Manager: SecureCredentialsManager,
    private val userRepository: UserRepository,
    private val preferencesService: PreferencesService,
    private val chatRepository: ChatRepository,
    private val tokenRepository: TokenRepository,
    connectivityRepository: ConnectivityRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unavailable
        )

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            when (val result = userRepository.getUserProfile()) {
                is Result.Error -> {

                }

                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(userProfile = result.data)
                    }
                }
            }
        }
    }

    fun startLogout(
        c: Context,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) {
        WebAuthProvider
            .logout(auth0Account)
            .withScheme(c.getString(R.string.com_auth0_scheme))
            .start(c, object :
                Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    onError()
                }

                override fun onSuccess(result: Void?) {
                    viewModelScope.launch {
                        tokenRepository.remove(
                            onSuccess = { message, processedCount -> },
                            onFailure = { message, details -> }
                        )
                        auth0Manager.clearCredentials()
                        chatRepository.disconnectUser()
                        preferencesService.clearUserCredentials()
                        onSuccess()
                    }
                }
            })
    }
}