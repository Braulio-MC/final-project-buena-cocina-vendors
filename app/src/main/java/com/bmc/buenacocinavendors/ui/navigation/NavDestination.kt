package com.bmc.buenacocinavendors.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmc.buenacocinavendors.R

enum class NavDestination(
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val contentDescription: Int,
    val route: String
) {
    HOME(
        label = R.string.navigation_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        contentDescription = R.string.navigation_home_content_desc,
        route = Screen.Main.Home.route
    ),
    ORDER(
        label = R.string.navigation_order,
        selectedIcon = Icons.Filled.LocalShipping,
        unselectedIcon = Icons.Outlined.LocalShipping,
        contentDescription = R.string.navigation_order_content_desc,
        route = Screen.Main.Order.route
    ),
    CHAT(
        label = R.string.navigation_chat,
        selectedIcon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        contentDescription = R.string.navigation_chat_content_desc,
        route = Screen.Main.Chat.route
    ),
    PROFILE(
        label = R.string.navigation_profile,
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        contentDescription = R.string.navigation_profile_content_desc,
        route = Screen.Main.Profile.route
    )
}
