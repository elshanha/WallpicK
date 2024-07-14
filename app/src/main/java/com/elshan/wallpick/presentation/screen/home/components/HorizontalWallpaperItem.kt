package com.elshan.wallpick.presentation.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.BaseColor
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.presentation.viewmodel.MainViewModel
import com.elshan.wallpick.utils.autoThemedPaletteColor

@Composable
fun HorizontalWallpaperItem(
    wallpaper: Wallpaper,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    navController: NavController
) {

    var dominantColor by remember {
        mutableStateOf(mainUiState.dominantColor)
    }
    var vibrantColor by remember {
        mutableStateOf(mainUiState.vibrantColor)
    }
    var mutedColor by remember {
        mutableStateOf(mainUiState.mutedColor)
    }

    ImageWithPalette(
        imageUrl = wallpaper.imageUrl,
        onColorExtracted = { palette ->
            dominantColor = Color(palette.getDominantColor(Color.White.toArgb()))
            vibrantColor = Color(palette.getVibrantColor(Color.White.toArgb()))
            mutedColor = Color(palette.getMutedColor(Color.White.toArgb()))
        }
    ) { imageWithPalette ->
        Box(
            modifier = Modifier
                .padding(
                    bottom = 8.dp,
                    start = 4.dp,
                    end = 4.dp
                )
                .clip(MaterialTheme.shapes.medium)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                mutedColor,
                                dominantColor,
                                vibrantColor,
                            ),
                            startY = 0f,
                        ),
                        shape = MaterialTheme.shapes.medium
                    ),
            ) {
                Box {
                    Image(
                        painter = imageWithPalette,
                        contentDescription = wallpaper.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(3f / 4f)
                            .clickable {
                                navController.navigate(
                                    Screen.WallpaperDetails(
                                        wallpaper.category,
                                        wallpaper.id,
                                        wallpaper.imageUrl,
                                        wallpaper.name,
                                        wallpaper.resolution,
                                        wallpaper.tags,
                                    )
                                )
                            },
                    )
                }


            }
        }
    }
}