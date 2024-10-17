package com.bmc.buenacocinavendors.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.data.network.model.GetStreamUserCredentials
import com.bmc.buenacocinavendors.data.preferences.PreferencesService
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.repository.ChatRepository
import com.bmc.buenacocinavendors.domain.repository.ConnectivityRepository
import com.bmc.buenacocinavendors.domain.repository.GetStreamTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth0Account: Auth0,
    private val auth0Manager: SecureCredentialsManager,
    private val preferencesService: PreferencesService,
    private val getStreamTokenRepository: GetStreamTokenRepository,
    private val chatRepository: ChatRepository,
    connectivityRepository: ConnectivityRepository
) : ViewModel() {
    val netState = connectivityRepository.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
            initialValue = NetworkStatus.Unavailable
        )

    fun startLogin(
        c: Context,
        onError: () -> Unit,
        onSuccess: (UserProfile) -> Unit
    ) {
        WebAuthProvider
            .login(auth0Account)
            .withScheme(c.getString(R.string.com_auth0_scheme))
            .start(c, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    onError()
                }

                override fun onSuccess(result: Credentials) {
                    viewModelScope.launch {
                        auth0Manager.saveCredentials(result)
                        val userId = result.user.getId()
                        val userName = result.user.name ?: result.user.nickname
                        val image = result.user.pictureURL

                        if (userId != null && userName != null && image != null) {
                            val id = userId.replace("|", "-")
                            when (val resultToken = getStreamTokenRepository.request(id)) {
                                is Result.Error -> {
                                    onError()
                                }

                                is Result.Success -> {
                                    val getStreamCredentials = GetStreamUserCredentials(
                                        apiKey = c.getString(R.string.get_stream_api_key),
                                        user = User(
                                            id = id,
                                            name = userName,
                                            image = image

                                        ),
                                        token = resultToken.data
                                    )
                                    preferencesService.saveUserCredentials(getStreamCredentials)
                                    chatRepository.connectUser(
                                        getStreamCredentials,
                                        onSuccess = { },
                                        onFailure = { e ->
                                            Log.e("LoginViewModel", "Error: ${e.message}")
                                        }
                                    )
                                }
                            }
                        }
                        onSuccess(result.user)
                    }
                }
            })
    }
}