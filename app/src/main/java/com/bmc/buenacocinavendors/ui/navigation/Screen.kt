package com.bmc.buenacocinavendors.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    sealed class Auth(val route: String) : Screen {
        data object Login : Auth("login")
    }

    sealed class StoreRegistration(val route: String) : Screen {
        data object Entry : StoreRegistration("entry")
        data object RegistrationForm : StoreRegistration("registration_form")
        data object RegistrationInformation : StoreRegistration("registration_information")
    }

    sealed class Main(val route: String) : Screen {
        data object Home : Main("home")
        data object StoreUpdateInformation : Main("store_update_information")
        data object Order : Main("order")
        data object Chat : Main("chat")
        data object Profile : Main("profile")
    }

    sealed class MainSerializable : Screen {
        @Serializable
        data class StoreUpdate(val storeId: String) : MainSerializable()

        @Serializable
        data class StoreVisualizer(val storeId: String) : MainSerializable()

        @Serializable
        data class Category(val storeId: String) : MainSerializable()

        @Serializable
        data class CategoryDetailed(val categoryId: String, val storeId: String) :
            MainSerializable()

        @Serializable
        data class CategoryGeneralDetailed(val categoryId: String) : MainSerializable()

        @Serializable
        data class Location(val storeId: String) : MainSerializable()

        @Serializable
        data class Discount(val storeId: String) : MainSerializable()

        @Serializable
        data class DiscountDetailed(val discountId: String, val storeId: String) :
            MainSerializable()

        @Serializable
        data class Product(val storeId: String, val storeName: String) : MainSerializable()

        @Serializable
        data class OrderDetailed(val orderId: String) : MainSerializable()

        @Serializable
        data class ChatDetailed(val channelId: String) : MainSerializable()
    }
}