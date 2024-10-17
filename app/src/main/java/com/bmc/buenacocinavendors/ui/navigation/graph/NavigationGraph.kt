package com.bmc.buenacocinavendors.ui.navigation.graph

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.auth0.android.result.UserProfile
import com.bmc.buenacocinavendors.ui.navigation.Graph
import com.bmc.buenacocinavendors.ui.navigation.NavigationState
import com.bmc.buenacocinavendors.ui.navigation.Screen
import com.bmc.buenacocinavendors.ui.screen.MainScreen
import com.bmc.buenacocinavendors.ui.screen.common.NavigationStateLoading
import com.bmc.buenacocinavendors.ui.viewmodel.NavigationViewModel
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    windowSizeClass: WindowSizeClass,
    channelViewModelFactory: ChannelViewModelFactory,
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onFinishActivity: () -> Unit,
    onHasStore: (String, UserProfile?) -> Unit
) {
    val result by produceState<NavigationState>(initialValue = NavigationState.Loading) {
        value = viewModel.checkNavigationState()
    }
    val popBackStack = {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            if (!navController.popBackStack()) {
                onFinishActivity()
            }
        }
    }

    if (result is NavigationState.Loading) {
        NavigationStateLoading()
        return
    }

    val startDestination = if (result is NavigationState.NotAuthenticated) {
        Graph.Auth.AuthGraph.route
    } else {
        if (result is NavigationState.HasStore) {
            Graph.Main.MainGraph.route
        } else {
            Graph.StoreRegistration.StoreRegistrationGraph.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            windowSizeClass = windowSizeClass,
            onLoginButton = { isSuccessful, userProfile ->
                if (isSuccessful) {
                    coroutineScope.launch {
                        val state = viewModel.checkNavigationState()
                        if (state is NavigationState.HasStore) {
                            onHasStore(state.storeId, userProfile)
                            navController.navigate(Graph.Main.MainGraph.route) {
                                popUpTo(Graph.Auth.AuthGraph.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        if (state is NavigationState.NotStore) {
                            navController.navigate(Graph.StoreRegistration.StoreRegistrationGraph.route) {
                                popUpTo(Graph.Auth.AuthGraph.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        )

        storeRegistrationGraph(
            windowSizeClass = windowSizeClass,
            onEntryStartButton = {
                navController.navigate(Screen.StoreRegistration.RegistrationForm.route) {
                    popUpTo(Screen.StoreRegistration.Entry.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onRegistrationFormLogoutButton = { isSuccessful ->
                if (isSuccessful) {
                    navController.navigate(Graph.Auth.AuthGraph.route) {
                        popUpTo(Graph.StoreRegistration.StoreRegistrationGraph.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            },
            onRegistrationFormSuccessfulRegistration = {
                navController.navigate(Graph.Auth.AuthGraph.route) {
                    popUpTo(Graph.StoreRegistration.StoreRegistrationGraph.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onRegistrationFormInformationButton = {
                navController.navigate(Screen.StoreRegistration.RegistrationInformation.route) {
                    launchSingleTop = true
                }
            },
            onRegistrationFormBackButton = { popBackStack() },
            onRegistrationInformationBackButton = { popBackStack() }
        )

        composable(Graph.Main.MainGraph.route) {
            MainScreen(
                windowSizeClass = windowSizeClass,
                channelViewModelFactory = channelViewModelFactory,
                onLogoutButton = { isSuccessful ->
                    if (isSuccessful) {
                        navController.navigate(Graph.Auth.AuthGraph.route) {
                            popUpTo(Graph.Main.MainGraph.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}