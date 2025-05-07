package com.bmc.buenacocinavendors.ui.screen.home.inner.product

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmc.buenacocinavendors.R

enum class ProductTabDestination(
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val contentDescription: Int,
) {
    ADD(
        label = R.string.product_screen_tab_navigation_add,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add,
        contentDescription = R.string.product_screen_tab_navigation_add_content_desc
    ),
    UPDATE(
        label = R.string.product_screen_tab_navigation_update,
        selectedIcon = Icons.Filled.Update,
        unselectedIcon = Icons.Outlined.Update,
        contentDescription = R.string.product_screen_tab_navigation_update_content_desc
    ),
    VISUALIZER(
        label = R.string.product_screen_tab_navigation_visualize,
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List,
        contentDescription = R.string.product_screen_tab_navigation_visualize
    )
}