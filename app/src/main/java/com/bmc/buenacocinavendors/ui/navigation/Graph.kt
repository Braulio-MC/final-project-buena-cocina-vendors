package com.bmc.buenacocinavendors.ui.navigation


sealed interface Graph {
    sealed class Auth(val route: String) : Graph {
        data object AuthGraph : Auth("auth_graph")
    }

    sealed class StoreRegistration(val route: String) : Graph {
        data object StoreRegistrationGraph : StoreRegistration("store_registration_graph")
    }

    sealed class Main(val route: String) : Graph {
        data object MainGraph : Main("main_graph")
    }
}