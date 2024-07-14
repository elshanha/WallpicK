package com.elshan.wallpick.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.elshan.wallpick.navigation.BottomScreen

sealed class BottomBarItems(
    val route: BottomScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomBarItems(
        route = BottomScreen.Home,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    data object Category : BottomBarItems(
        route = BottomScreen.Category,
        label = "Category",
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category
        )

    data object Favorite : BottomBarItems(
        route = BottomScreen.Favorite,
        label = "Favorite",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.Favorite
    )
}