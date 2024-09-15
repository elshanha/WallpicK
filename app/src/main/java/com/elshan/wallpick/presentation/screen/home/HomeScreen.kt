package com.elshan.wallpick.presentation.screen.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.components.CustomTopAppBar
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    gridScrollState: LazyGridState,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = scrollBehavior,
                navController = navController,
                title = "WallPicK",
                selectedFilter = mainUiState.selectedFilter,
                onFilterSelected = { filterType ->
                    onEvent(MainUiEvents.FilterWallpapers(filterType))
                }
            )
        }
    ) { paddingValue ->

        WallpaperList(
            mainUiState,
            navController,
            onEvent,
            scrollBehavior,
            paddingValue,
            wallpaperList = mainUiState.filteredWallpapers.ifEmpty {
                mainUiState.allWallpapers
            },
            gridListState = gridScrollState
        )

    }
}
