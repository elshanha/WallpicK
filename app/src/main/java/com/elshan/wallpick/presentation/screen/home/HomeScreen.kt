package com.elshan.wallpick.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.components.CustomTopAppBar
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.animation.AnimatedText
import com.elshan.wallpick.utils.custom.FilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    gridScrollState: LazyGridState,
) {
//    var selectedFilter by remember { mutableStateOf(mainUiState.selectedFilter) }
//
//    LaunchedEffect(mainUiState.selectedFilter) {
//        selectedFilter = mainUiState.selectedFilter
//    }

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
            wallpaperList = mainUiState.filteredWallpapers,
            gridListState = gridScrollState
        )

    }
}


//     LazyColumn(
//            state = listScrollState,
//            modifier = Modifier
//                .fillMaxSize()
//                .nestedScroll(scrollBehavior.nestedScrollConnection)
//                .padding(paddingValues)
//            ,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                WallpaperSection(
//                    title = "All",
//                    wallpapers = mainUiState.allWallpapers,
//                    onEvent,
//                    mainUiState,
//                    navController,
//                    screen = Screen.AllWallpapers
//                )
//            }
//            item {
//                WallpaperSection(
//                    title = "Popular",
//                    wallpapers = mainUiState.popularWallpapers,
//                    onEvent,
//                    mainUiState,
//                    navController,
//                    screen = Screen.PopularWallpapers
//                )
//            }
//            item {
//                WallpaperSection(
//                    title = "Most Downloaded",
//                    wallpapers = mainUiState.mostDownloadedWallpapers,
//                     onEvent,
//                    mainUiState,
//                    navController,
//                    screen = Screen.MostDownloadedWallpapers
//                )
//            }
//        }


//
//@Composable
//private fun BottomSection(navController: NavController, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .horizontalWindowInsetsPadding()
//            .bottomWindowInsetsPadding()
//            .padding(horizontal = 20.dp)
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Row(
//                modifier = modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//
//                CustomButton(
//                    modifier = modifier.weight(1f),
//                    text = "Categories",
//                    icon = Icons.Outlined.Category
//                ) {
//                    navController.navigate(
//                        Screen.Category
//                    )
//                }
//
//                CustomButton(
//                    modifier = modifier.weight(1f),
//                    text = "Favorites",
//                    icon = Icons.Outlined.Favorite
//                ) {
//                    navController.navigate(
//                        Screen.Favorites
//                    )
//                }
//                CustomButton(
//                    modifier = modifier.weight(1f),
//                    text = "Profile",
//                    icon = Icons.Outlined.AccountCircle
//                ) {
//                    navController.navigate(
//                        Screen.Profile(
//                            title = "Profile", description = "Description"
//                        )
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//
