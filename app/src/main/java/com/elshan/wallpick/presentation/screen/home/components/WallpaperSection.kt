package com.elshan.wallpick.presentation.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState

@Composable
fun WallpaperSection(
    title: String,
    wallpapers: List<Wallpaper>,
    onEvent: (MainUiEvents) -> Unit,
    mainUiState: MainUiState,
    navController: NavController,
    screen: Screen
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            TextButton(onClick = {
                navController.navigate(screen)
            }) {
                Text(text = "See All", style = MaterialTheme.typography.bodyLarge)
            }
        }
        LazyRow(

            modifier = Modifier.aspectRatio(3f / 2f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(wallpapers.take(10)) { wallpaper ->
                HorizontalWallpaperItem(
                    wallpaper,
                    navController = navController,
                    onEvent = onEvent,
                    mainUiState = mainUiState
                )
            }
        }
    }
}