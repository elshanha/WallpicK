package com.elshan.wallpick.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.animation.AnimatedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperList(
    mainUiState: MainUiState,
    navController: NavController,
    onEvent: (MainUiEvents) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues,
    wallpaperList: List<Wallpaper>,
    gridListState: LazyGridState = rememberLazyGridState()
) {

    if (wallpaperList.isNotEmpty()) {
        LazyVerticalGrid(
            state = gridListState,
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

            items(wallpaperList) { wallpaper ->

                if (wallpaperList.isNotEmpty()) {
                    WallpaperItem(
                        wallpaper = wallpaper,
                        navController,
                        mainUiState,
                        onEvent,
                    )
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
    } else {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedText(firstText = "Loading...", secondText = "No wallpapers found :(")
        }
    }
}