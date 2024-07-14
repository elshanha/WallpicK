package com.elshan.wallpick.presentation.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.screen.home.WallpaperItem
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.viewmodel.MainViewModel
import com.elshan.wallpick.utils.topBarColors
import com.elshan.wallpick.utils.topWindowInsetsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizedWallpapers(
    mainUiState: MainUiState,
    navController: NavController,
    onEvent: (MainUiEvents) -> Unit,
    category: String
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = category, style = MaterialTheme.typography.titleLarge)
                },
                colors = topBarColors(),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        val paddingValues = it

        if (mainUiState.wallpapersByCategory.isNotEmpty()) {
            LazyVerticalGrid(
                state = rememberLazyGridState(),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                items(mainUiState.wallpapersByCategory) { wallpaper ->

                    if (mainUiState.wallpapersByCategory.isNotEmpty()) {
                        WallpaperItem(
                            wallpaper = wallpaper,
                            navController = navController,
                            mainUiState = mainUiState,
                            onEvent = onEvent,
                        )
                    }
                }

            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No wallpapers found")
            }
        }
    }
}