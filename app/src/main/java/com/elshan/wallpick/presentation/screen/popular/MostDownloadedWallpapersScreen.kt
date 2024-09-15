package com.elshan.wallpick.presentation.screen.popular

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.components.CustomTopAppBar
import com.elshan.wallpick.presentation.screen.home.WallpaperList
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostDownloadedWallpapersScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior,
                navController,
                "Most Downloaded",
                onFilterSelected = { filterType ->
                    onEvent(MainUiEvents.FilterWallpapers(filterType))
                })

        }
    ) { paddingValues ->
        val paddingValues = paddingValues

//        WallpaperList(
//            mainUiState,
//            navController,
//            onEvent,
//            scrollBehavior,
//            paddingValues,
//            wallpaperList = mainUiState.mostDownloadedWallpapers
//        )
    }
}
