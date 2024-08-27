package com.bmc.buenacocinavendors.ui.navigation.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bmc.buenacocinavendors.ui.navigation.Graph
import com.bmc.buenacocinavendors.ui.navigation.Screen
import com.bmc.buenacocinavendors.ui.screen.store.StoreRegistrationEntryScreen
import com.bmc.buenacocinavendors.ui.screen.store.StoreRegistrationInformationScreen
import com.bmc.buenacocinavendors.ui.screen.store.StoreRegistrationScreen

fun NavGraphBuilder.storeRegistrationGraph(
    windowSizeClass: WindowSizeClass,
    onEntryStartButton: () -> Unit,
    onRegistrationFormLogoutButton: (Boolean) -> Unit,
    onRegistrationFormSuccessfulRegistration: () -> Unit,
    onRegistrationFormInformationButton: () -> Unit,
    onRegistrationFormBackButton: () -> Unit,
    onRegistrationInformationBackButton: () -> Unit
) {
    navigation(
        startDestination = Screen.StoreRegistration.Entry.route,
        route = Graph.StoreRegistration.StoreRegistrationGraph.route
    ) {
        entryScreen(
            windowSizeClass = windowSizeClass,
            onStartButton = onEntryStartButton
        )
        registrationFormScreen(
            windowSizeClass = windowSizeClass,
            onLogoutButton = onRegistrationFormLogoutButton,
            onSuccessfulRegistration = onRegistrationFormSuccessfulRegistration,
            onInformationButton = onRegistrationFormInformationButton,
            onBackButton = onRegistrationFormBackButton
        )
        registrationInformationScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onRegistrationInformationBackButton
        )
    }
}

fun NavGraphBuilder.entryScreen(
    windowSizeClass: WindowSizeClass,
    onStartButton: () -> Unit
) {
    composable(Screen.StoreRegistration.Entry.route) {
        StoreRegistrationEntryScreen(
            windowSizeClass = windowSizeClass,
            onStartButton = onStartButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.registrationFormScreen(
    windowSizeClass: WindowSizeClass,
    onLogoutButton: (Boolean) -> Unit,
    onSuccessfulRegistration: () -> Unit,
    onInformationButton: () -> Unit,
    onBackButton: () -> Unit
) {
    composable(Screen.StoreRegistration.RegistrationForm.route) {
        StoreRegistrationScreen(
            windowSizeClass = windowSizeClass,
            onLogoutButton = onLogoutButton,
            onSuccessfulRegistration = onSuccessfulRegistration,
            onInformationButton = onInformationButton,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.registrationInformationScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable(Screen.StoreRegistration.RegistrationInformation.route) {
        StoreRegistrationInformationScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onBackButton
        )
    }
}