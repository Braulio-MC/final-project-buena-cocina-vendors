package com.bmc.buenacocinavendors.ui.screen.home.inner.location

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.bmc.buenacocinavendors.R

enum class LocationTabDestination(
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val contentDescription: Int,
) {
    ADD(
        label = R.string.location_screen_tab_navigation_add,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add,
        contentDescription = R.string.location_screen_tab_navigation_add_content_desc
    ),
    UPDATE(
        label = R.string.location_screen_tab_navigation_update,
        selectedIcon = Icons.Filled.Update,
        unselectedIcon = Icons.Outlined.Update,
        contentDescription = R.string.location_screen_tab_navigation_update_content_desc,
    ),
    DELETE(
        label = R.string.location_screen_tab_navigation_delete,
        selectedIcon = Icons.Filled.Delete,
        unselectedIcon = Icons.Outlined.Delete,
        contentDescription = R.string.location_screen_tab_navigation_delete_content_desc,
    ),
    VISUALIZER(
        label = R.string.location_screen_tab_navigation_visualize,
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List,
        contentDescription = R.string.location_screen_tab_navigation_visualize,
    ),
}