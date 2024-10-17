package com.bmc.buenacocinavendors.ui.navigation.graph

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bmc.buenacocinavendors.ui.navigation.Graph
import com.bmc.buenacocinavendors.ui.navigation.Screen
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

@Composable
fun MainGraph(
    windowSizeClass: WindowSizeClass,
    channelViewModelFactory: ChannelViewModelFactory,
    navController: NavHostController,
    onStoreUpdateBackButton: () -> Unit,
    onStoreUpdateInformationBackButton: () -> Unit,
    onStoreVisualizerBackButton: () -> Unit,
    onCategoryBackButton: () -> Unit,
    onCategoryVisualizerItemDetailedBackButton: () -> Unit,
    onCategoryGeneralItemDetailedBackButton: () -> Unit,
    onLocationBackButton: () -> Unit,
    onDiscountBackButton: () -> Unit,
    onDiscountVisualizerItemDetailedBackButton: () -> Unit,
    onProductBackButton: () -> Unit,
    onOrderBackButton: () -> Unit,
    onOrderDetailedBackButton: () -> Unit,
    onChatBackButton: () -> Unit,
    onDetailedChatBackButton: () -> Unit,
    onLogoutButton: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Graph.Main.MainGraph.route
    ) {
        mainGraph(
            windowSizeClass = windowSizeClass,
            channelViewModelFactory = channelViewModelFactory,
            onStoreUpdateButton = { storeId ->
                navController.navigate(Screen.MainSerializable.StoreUpdate(storeId)) {
                    launchSingleTop = true
                }
            },
            onStoreUpdateSuccessful = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.StoreUpdate> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onStoreUpdateBackButton = onStoreUpdateBackButton,
            onStoreUpdateInformationButton = {
                navController.navigate(Screen.Main.StoreUpdateInformation.route) {
                    launchSingleTop = true
                }
            },
            onStoreUpdateInformationBackButton = onStoreUpdateInformationBackButton,
            onStoreVisualizerButton = { storeId ->
                navController.navigate(Screen.MainSerializable.StoreVisualizer(storeId)) {
                    launchSingleTop = true
                }
            },
            onStoreVisualizerBackButton = onStoreVisualizerBackButton,
            onCategoryButton = { storeId ->
                navController.navigate(Screen.MainSerializable.Category(storeId)) {
                    launchSingleTop = true
                }
            },
            onCategoryBackButton = onCategoryBackButton,
            onCategoryVisualizerItemClick = { categoryId, storeId ->
                navController.navigate(
                    Screen.MainSerializable.CategoryDetailed(
                        categoryId,
                        storeId
                    )
                ) {
                    launchSingleTop = true
                }
            },
            onCategoryGeneralItemClick = { categoryId ->
                navController.navigate(Screen.MainSerializable.CategoryGeneralDetailed(categoryId)) {
                    launchSingleTop = true
                }
            },
            onCategoryVisualizerItemDetailedBackButton = onCategoryVisualizerItemDetailedBackButton,
            onCategoryGeneralItemDetailedBackButton = onCategoryGeneralItemDetailedBackButton,
            onCategorySuccessfulCreation = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Category> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onCategorySuccessfulUpdate = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Category> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onCategorySuccessfulDelete = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Category> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onLocationButton = { storeId ->
                navController.navigate(Screen.MainSerializable.Location(storeId)) {
                    launchSingleTop = true
                }
            },
            onLocationBackButton = onLocationBackButton,
            onLocationSuccessfulCreation = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Location> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onLocationSuccessfulUpdate = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Location> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onLocationSuccessfulDelete = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Location> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onDiscountButton = { storeId ->
                navController.navigate(Screen.MainSerializable.Discount(storeId)) {
                    launchSingleTop = true
                }
            },
            onDiscountBackButton = onDiscountBackButton,
            onDiscountVisualizerItemClick = { discountId, storeId ->
                navController.navigate(
                    Screen.MainSerializable.DiscountDetailed(
                        discountId,
                        storeId
                    )
                ) {
                    launchSingleTop = true
                }
            },
            onDiscountVisualizerItemDetailedBackButton = onDiscountVisualizerItemDetailedBackButton,
            onDiscountSuccessfulCreation = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Discount> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onDiscountSuccessfulUpdate = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Discount> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onDiscountSuccessfulDelete = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Discount> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onProductButton = { storeId, storeName ->
                navController.navigate(Screen.MainSerializable.Product(storeId, storeName)) {
                    launchSingleTop = true
                }
            },
            onProductBackButton = onProductBackButton,
            onProductSuccessfulCreation = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Product> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onProductSuccessfulUpdate = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Product> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onProductSuccessfulDelete = {
                navController.navigate(Screen.Main.Home.route) {
                    popUpTo<Screen.MainSerializable.Product> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onOrderBackButton = onOrderBackButton,
            onOrderItemClick = { orderId ->
                navController.navigate(Screen.MainSerializable.OrderDetailed(orderId)) {
                    launchSingleTop = true
                }
            },
            onOrderDetailedBackButton = onOrderDetailedBackButton,
            onOrderDetailedChannelCreatedSuccessful = { channelId ->
                navController.navigate(Screen.MainSerializable.ChatDetailed(channelId)) {
                    popUpTo(Screen.Main.Order.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onChatBackButton = onChatBackButton,
            onChatItemClick = { channel ->
                navController.navigate(Screen.MainSerializable.ChatDetailed(channel.cid)) {
                    launchSingleTop = true
                }
            },
            onDetailedChatBackButton = onDetailedChatBackButton,
            onLogoutButton = onLogoutButton
        )
    }
}