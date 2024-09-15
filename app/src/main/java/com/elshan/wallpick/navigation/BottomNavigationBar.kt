package com.elshan.wallpick.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.elshan.wallpick.presentation.components.BottomBarItems
import com.elshan.wallpick.presentation.screen.category.CategoryScreen
import com.elshan.wallpick.presentation.screen.favorite.FavoritesScreen
import com.elshan.wallpick.presentation.screen.home.HomeScreen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState

@Composable
fun BottomNavigationBar(
    bottomBarNavController: NavHostController,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomBarItems.Home,
        BottomBarItems.Category,
        BottomBarItems.Favorite,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 75.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
                .height(56.dp)
                .align(Alignment.Center)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val isSelectedItem = currentRoute?.endsWith(item.route.toString()) ?: false
                    NavigationBarItem(
                        selected = isSelectedItem,
                        onClick = {
                            bottomBarNavController.navigate(item.route) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelectedItem) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (isSelectedItem) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,

                                )
                        },
                    )
                }
            }
        }
    }
}


@Composable
fun BottomBarScreens(
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    listScrollState: LazyListState,
    gridScrollState: LazyGridState,
) {

    NavHost(
        modifier = modifier,
        navController = bottomBarNavController,
        startDestination = BottomScreen.Home
    ) {

        composable<BottomScreen.Home> {
            HomeScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent,
                gridScrollState = gridScrollState,
            )
        }

        composable<BottomScreen.Category> {
            CategoryScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent,
                gridScrollState = gridScrollState,
                listScrollState = listScrollState,
            )
        }

        composable<BottomScreen.Favorite> {
            FavoritesScreen(
                mainUiState = mainUiState,
                onEvent = onEvent,
                navController = navController,
                gridScrollState = gridScrollState,
            )
        }
    }
}