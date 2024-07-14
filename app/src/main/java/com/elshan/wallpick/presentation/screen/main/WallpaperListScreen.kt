package com.elshan.wallpick.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun WallpaperListScreen(
    selectedItem: MutableState<Int>,
    navController: NavController,
    bottomBarNavController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
) {




}