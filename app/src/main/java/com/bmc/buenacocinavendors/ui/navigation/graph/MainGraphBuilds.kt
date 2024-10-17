package com.bmc.buenacocinavendors.ui.navigation.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.bmc.buenacocinavendors.ui.navigation.Graph
import com.bmc.buenacocinavendors.ui.navigation.Screen
import com.bmc.buenacocinavendors.ui.screen.chat.ChatScreen
import com.bmc.buenacocinavendors.ui.screen.chat.DetailedChatScreen
import com.bmc.buenacocinavendors.ui.screen.home.HomeScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.CategoryScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.general.detailed.CategoryTabGeneralItemDetailed
import com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.visualizer.detailed.CategoryTabVisualizerItemDetailed
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.DiscountScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.discount.tabs.visualizer.detailed.DiscountTabVisualizerItemDetailed
import com.bmc.buenacocinavendors.ui.screen.home.inner.location.LocationScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.product.ProductScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.store.update.StoreUpdateInformationScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.store.update.StoreUpdateScreen
import com.bmc.buenacocinavendors.ui.screen.home.inner.store.visualizer.StoreVisualizerScreen
import com.bmc.buenacocinavendors.ui.screen.order.OrderScreen
import com.bmc.buenacocinavendors.ui.screen.order.detailed.DetailedOrderScreen
import com.bmc.buenacocinavendors.ui.screen.profile.ProfileScreen
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.Channel

fun NavGraphBuilder.mainGraph(
    windowSizeClass: WindowSizeClass,
    channelViewModelFactory: ChannelViewModelFactory,
    onStoreUpdateButton: (String) -> Unit,
    onStoreUpdateSuccessful: () -> Unit,
    onStoreUpdateBackButton: () -> Unit,
    onStoreUpdateInformationButton: () -> Unit,
    onStoreUpdateInformationBackButton: () -> Unit,
    onStoreVisualizerButton: (String) -> Unit,
    onStoreVisualizerBackButton: () -> Unit,
    onCategoryButton: (String) -> Unit,
    onCategoryBackButton: () -> Unit,
    onCategoryVisualizerItemClick: (String, String) -> Unit,
    onCategoryGeneralItemClick: (String) -> Unit,
    onCategoryVisualizerItemDetailedBackButton: () -> Unit,
    onCategoryGeneralItemDetailedBackButton: () -> Unit,
    onCategorySuccessfulCreation: () -> Unit,
    onCategorySuccessfulUpdate: () -> Unit,
    onCategorySuccessfulDelete: () -> Unit,
    onLocationButton: (String) -> Unit,
    onLocationBackButton: () -> Unit,
    onLocationSuccessfulCreation: () -> Unit,
    onLocationSuccessfulUpdate: () -> Unit,
    onLocationSuccessfulDelete: () -> Unit,
    onDiscountButton: (String) -> Unit,
    onDiscountBackButton: () -> Unit,
    onDiscountVisualizerItemClick: (String, String) -> Unit,
    onDiscountVisualizerItemDetailedBackButton: () -> Unit,
    onDiscountSuccessfulCreation: () -> Unit,
    onDiscountSuccessfulUpdate: () -> Unit,
    onDiscountSuccessfulDelete: () -> Unit,
    onProductButton: (String, String) -> Unit,
    onProductBackButton: () -> Unit,
    onProductSuccessfulCreation: () -> Unit,
    onProductSuccessfulUpdate: () -> Unit,
    onProductSuccessfulDelete: () -> Unit,
    onOrderBackButton: () -> Unit,
    onOrderItemClick: (String) -> Unit,
    onOrderDetailedBackButton: () -> Unit,
    onOrderDetailedChannelCreatedSuccessful: (String) -> Unit,
    onChatBackButton: () -> Unit,
    onChatItemClick: (Channel) -> Unit,
    onDetailedChatBackButton: () -> Unit,
    onLogoutButton: (Boolean) -> Unit,
) {
    navigation(
        startDestination = Screen.Main.Home.route,
        route = Graph.Main.MainGraph.route
    ) {
        homeScreen(
            windowSizeClass = windowSizeClass,
            onStoreUpdateButton = onStoreUpdateButton,
            onStoreVisualizerButton = onStoreVisualizerButton,
            onCategoryButton = onCategoryButton,
            onLocationButton = onLocationButton,
            onDiscountButton = onDiscountButton,
            onProductButton = onProductButton
        )
        storeUpdateScreen(
            windowSizeClass = windowSizeClass,
            onSuccessfulUpdate = onStoreUpdateSuccessful,
            onBackButton = onStoreUpdateBackButton,
            onInformationButton = onStoreUpdateInformationButton
        )
        storeUpdateInformationScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onStoreUpdateInformationBackButton
        )
        storeVisualizerScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onStoreVisualizerBackButton
        )
        categoryScreen(
            windowSizeClass = windowSizeClass,
            onCategoryVisualizerItemClick = onCategoryVisualizerItemClick,
            onCategoryGeneralItemClick = onCategoryGeneralItemClick,
            onCategorySuccessfulCreation = onCategorySuccessfulCreation,
            onCategorySuccessfulUpdate = onCategorySuccessfulUpdate,
            onCategorySuccessfulDelete = onCategorySuccessfulDelete,
            onBackButton = onCategoryBackButton
        )
        categoryDetailedScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onCategoryVisualizerItemDetailedBackButton
        )
        categoryGeneralDetailedScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onCategoryGeneralItemDetailedBackButton
        )
        locationScreen(
            windowSizeClass = windowSizeClass,
            onLocationSuccessfulCreation = onLocationSuccessfulCreation,
            onLocationSuccessfulUpdate = onLocationSuccessfulUpdate,
            onLocationSuccessfulDelete = onLocationSuccessfulDelete,
            onBackButton = onLocationBackButton
        )
        discountScreen(
            windowSizeClass = windowSizeClass,
            onDiscountVisualizerItemClick = onDiscountVisualizerItemClick,
            onDiscountSuccessfulCreation = onDiscountSuccessfulCreation,
            onDiscountSuccessfulUpdate = onDiscountSuccessfulUpdate,
            onDiscountSuccessfulDelete = onDiscountSuccessfulDelete,
            onBackButton = onDiscountBackButton
        )
        discountDetailedScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onDiscountVisualizerItemDetailedBackButton
        )
        productScreen(
            windowSizeClass = windowSizeClass,
            onProductSuccessfulCreation = onProductSuccessfulCreation,
            onProductSuccessfulUpdate = onProductSuccessfulUpdate,
            onProductSuccessfulDelete = onProductSuccessfulDelete,
            onBackButton = onProductBackButton
        )
        orderScreen(
            windowSizeClass = windowSizeClass,
            onOrderItemClick = onOrderItemClick,
            onBackButton = onOrderBackButton
        )
        orderDetailedScreen(
            windowSizeClass,
            onChannelCreatedSuccessful = onOrderDetailedChannelCreatedSuccessful,
            onBackButton = onOrderDetailedBackButton

        )
        chatScreen(
            viewModel = channelViewModelFactory,
            onItemClick = onChatItemClick,
            onBackButton = onChatBackButton
        )
        detailedChatScreen(
            onBackButton = onDetailedChatBackButton
        )
        profileScreen(
            windowSizeClass = windowSizeClass,
            onLogoutButton = onLogoutButton
        )
    }
}

fun NavGraphBuilder.homeScreen(
    windowSizeClass: WindowSizeClass,
    onStoreUpdateButton: (String) -> Unit,
    onStoreVisualizerButton: (String) -> Unit,
    onCategoryButton: (String) -> Unit,
    onLocationButton: (String) -> Unit,
    onDiscountButton: (String) -> Unit,
    onProductButton: (String, String) -> Unit
) {
    composable(Screen.Main.Home.route) {
        HomeScreen(
            windowSizeClass = windowSizeClass,
            onStoreUpdateButton = onStoreUpdateButton,
            onStoreVisualizerButton = onStoreVisualizerButton,
            onCategoryButton = onCategoryButton,
            onLocationButton = onLocationButton,
            onDiscountButton = onDiscountButton,
            onProductButton = onProductButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.storeUpdateScreen(
    windowSizeClass: WindowSizeClass,
    onSuccessfulUpdate: () -> Unit,
    onBackButton: () -> Unit,
    onInformationButton: () -> Unit
) {
    composable<Screen.MainSerializable.StoreUpdate> {
        val result = it.toRoute<Screen.MainSerializable.StoreUpdate>()
        StoreUpdateScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            onSuccessfulUpdate = onSuccessfulUpdate,
            onBackButton = onBackButton,
            onInformationButton = onInformationButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.storeUpdateInformationScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable(Screen.Main.StoreUpdateInformation.route) {
        StoreUpdateInformationScreen(
            windowSizeClass = windowSizeClass,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.storeVisualizerScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.StoreVisualizer> {
        val result = it.toRoute<Screen.MainSerializable.StoreVisualizer>()
        StoreVisualizerScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.categoryScreen(
    windowSizeClass: WindowSizeClass,
    onCategoryVisualizerItemClick: (String, String) -> Unit,
    onCategoryGeneralItemClick: (String) -> Unit,
    onCategorySuccessfulCreation: () -> Unit,
    onCategorySuccessfulUpdate: () -> Unit,
    onCategorySuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.Category> {
        val result = it.toRoute<Screen.MainSerializable.Category>()
        CategoryScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            onCategoryVisualizerItemClick = onCategoryVisualizerItemClick,
            onCategoryGeneralItemClick = onCategoryGeneralItemClick,
            onCategorySuccessfulCreation = onCategorySuccessfulCreation,
            onCategorySuccessfulUpdate = onCategorySuccessfulUpdate,
            onCategorySuccessfulDelete = onCategorySuccessfulDelete,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.categoryDetailedScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.CategoryDetailed> {
        val result = it.toRoute<Screen.MainSerializable.CategoryDetailed>()
        CategoryTabVisualizerItemDetailed(
            windowSizeClass = windowSizeClass,
            categoryId = result.categoryId,
            storeId = result.storeId,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.categoryGeneralDetailedScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.CategoryGeneralDetailed> {
        val result = it.toRoute<Screen.MainSerializable.CategoryGeneralDetailed>()
        CategoryTabGeneralItemDetailed(
            windowSizeClass = windowSizeClass,
            categoryId = result.categoryId,
            onBackButton = onBackButton
        )
    }
}

fun NavGraphBuilder.locationScreen(
    windowSizeClass: WindowSizeClass,
    onLocationSuccessfulCreation: () -> Unit,
    onLocationSuccessfulUpdate: () -> Unit,
    onLocationSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.Location> {
        val result = it.toRoute<Screen.MainSerializable.Location>()
        LocationScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            onLocationSuccessfulCreation = onLocationSuccessfulCreation,
            onLocationSuccessfulUpdate = onLocationSuccessfulUpdate,
            onLocationSuccessfulDelete = onLocationSuccessfulDelete,
            onBackButton = onBackButton
        )
    }
}

fun NavGraphBuilder.discountScreen(
    windowSizeClass: WindowSizeClass,
    onDiscountVisualizerItemClick: (String, String) -> Unit,
    onDiscountSuccessfulCreation: () -> Unit,
    onDiscountSuccessfulUpdate: () -> Unit,
    onDiscountSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.Discount> {
        val result = it.toRoute<Screen.MainSerializable.Discount>()
        DiscountScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            onDiscountVisualizerItemClick = onDiscountVisualizerItemClick,
            onDiscountSuccessfulCreation = onDiscountSuccessfulCreation,
            onDiscountSuccessfulUpdate = onDiscountSuccessfulUpdate,
            onDiscountSuccessfulDelete = onDiscountSuccessfulDelete,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.discountDetailedScreen(
    windowSizeClass: WindowSizeClass,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.DiscountDetailed> {
        val result = it.toRoute<Screen.MainSerializable.DiscountDetailed>()
        DiscountTabVisualizerItemDetailed(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            discountId = result.discountId,
            onBackButton = onBackButton
        )
    }
}

fun NavGraphBuilder.productScreen(
    windowSizeClass: WindowSizeClass,
    onProductSuccessfulCreation: () -> Unit,
    onProductSuccessfulUpdate: () -> Unit,
    onProductSuccessfulDelete: () -> Unit,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.Product> {
        val result = it.toRoute<Screen.MainSerializable.Product>()
        ProductScreen(
            windowSizeClass = windowSizeClass,
            storeId = result.storeId,
            storeName = result.storeName,
            onProductSuccessfulCreation = onProductSuccessfulCreation,
            onProductSuccessfulUpdate = onProductSuccessfulUpdate,
            onProductSuccessfulDelete = onProductSuccessfulDelete,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.orderScreen(
    windowSizeClass: WindowSizeClass,
    onOrderItemClick: (String) -> Unit,
    onBackButton: () -> Unit
) {
    composable(Screen.Main.Order.route) {
        OrderScreen(
            windowSizeClass = windowSizeClass,
            onOrderItemClick = onOrderItemClick,
            onBackButton = onBackButton
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.orderDetailedScreen(
    windowSizeClass: WindowSizeClass,
    onChannelCreatedSuccessful: (String) -> Unit,
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.OrderDetailed> {
        val result = it.toRoute<Screen.MainSerializable.OrderDetailed>()
        DetailedOrderScreen(
            windowSizeClass = windowSizeClass,
            orderId = result.orderId,
            onChannelCreatedSuccessful = onChannelCreatedSuccessful,
            onBackButton = onBackButton
        )
    }
}

fun NavGraphBuilder.chatScreen(
    viewModel: ChannelViewModelFactory,
    onItemClick: (Channel) -> Unit,
    onBackButton: () -> Unit
) {
    composable(Screen.Main.Chat.route) {
        ChatScreen(
            viewModel = viewModel,
            onItemClick = onItemClick,
            onBackButton = onBackButton
        )
    }
}

fun NavGraphBuilder.detailedChatScreen(
    onBackButton: () -> Unit
) {
    composable<Screen.MainSerializable.ChatDetailed> {
        val nav = it.toRoute<Screen.MainSerializable.ChatDetailed>()
        DetailedChatScreen(
            channelId = nav.channelId,
            onBackButton = onBackButton
        )
    }
}


fun NavGraphBuilder.profileScreen(
    windowSizeClass: WindowSizeClass,
    onLogoutButton: (Boolean) -> Unit
) {
    composable(Screen.Main.Profile.route) {
        ProfileScreen(
            windowSizeClass = windowSizeClass,
            onLogoutButton = onLogoutButton
        )
    }
}