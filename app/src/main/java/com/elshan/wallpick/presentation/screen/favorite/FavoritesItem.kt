package com.elshan.wallpick.presentation.screen.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.utils.animation.FavoriteHeartButton
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.presentation.viewmodel.MainViewModel

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun FavoriteImageItem(
    wallpaperEntity: WallpaperEntity,
    navController: NavController,
    onEvent: (MainUiEvents) -> Unit,
) {
    val context = LocalContext.current

    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    ImageWithPalette(
        imageUrl = wallpaperEntity.imageUrl,
        onColorExtracted = { palette ->
            onEvent(MainUiEvents.SetPaletteColors(palette))
        }
    ) { imagePainter ->
        Box(
            modifier = Modifier
                .padding(
                    bottom = 8.dp,
                    start = 4.dp,
                    end = 4.dp
                )
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                dominantColor,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
            ) {
                Box {
                    Image(
                        painter = imagePainter,
                        contentDescription = wallpaperEntity.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(3f / 4f)
                            .combinedClickable(
                                onClick = {
                                    navController.navigate(
                                        Screen.FullScreenImage(
                                            wallpaperEntity.category,
                                            wallpaperEntity.imageUrl,
                                            wallpaperEntity.name,
                                            wallpaperEntity.resolution,
                                            wallpaperEntity.id
                                        )
                                    )
                                },
                                onLongClick = {}
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        FavoriteHeartButton(
                            color = dominantColor,
                            isFavorite = true
                        ) {
                            onEvent(MainUiEvents.RemoveFromFavorite(wallpaperEntity))
                            onEvent(
                                MainUiEvents.ShowSnackBar(
                                    message = "Removed from favorites",
                                    isBottomAppBarVisible = true
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}
