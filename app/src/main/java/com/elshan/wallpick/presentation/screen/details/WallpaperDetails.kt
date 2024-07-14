package com.elshan.wallpick.presentation.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.main.data.mappers.toWallpaperEntity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.screen.details.component.ButtonBox
import com.elshan.wallpick.presentation.screen.details.component.DetailBox
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.BaseColor
import com.elshan.wallpick.utils.animation.FavoriteHeartButton
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.utils.autoThemedPaletteColor
//
//@Composable
//fun WallpaperDetails(
//    modifier: Modifier = Modifier,
//    wallpaper: Wallpaper,
//    mainUiState: MainUiState,
//    navController: NavController,
//    onEvent: (MainUiEvents) -> Unit
//) {
//    ImageWithPalette(
//        imageUrl = wallpaper.imageUrl,
//        onColorExtracted = { palette ->
//            onEvent(MainUiEvents.SetPaletteColors(palette))
//        },
//    ) { imagePainter ->
//
//        Column(
//            modifier = modifier
//                .background(color = MaterialTheme.colorScheme.background)
//                .fillMaxSize()
//                .statusBarsPadding(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = wallpaper.name,
//                style = MaterialTheme.typography.titleLarge.copy(
//                    color = autoThemedPaletteColor(
//                        mainUiState = mainUiState,
//                        color = BaseColor.Vibrant
//                    )
//                ),
//                modifier = modifier.padding(top = 16.dp)
//            )
//            Box(
//                modifier = modifier
//                    .padding(horizontal = 16.dp, vertical = 16.dp)
//            ) {
//
//                Image(
//                    painter = imagePainter,
//                    contentDescription = wallpaper.name,
//                    contentScale = ContentScale.Crop,
//                    modifier = modifier
////                        .aspectRatio(9f / 16f)
//                        .clickable {
//                            navController.navigate(
//                                Screen.FullScreenImage(
//                                    imageUrl = wallpaper.imageUrl,
//                                    name = wallpaper.name,
//                                    resolution = wallpaper.resolution,
//                                    category = wallpaper.category,
//                                    id = wallpaper.id
//                                )
//                            )
//                            onEvent(
//                                MainUiEvents.TrackEvent(
//                                    wallpaper.category,
//                                    wallpaper.id,
//                                    "views"
//                                )
//                            )
//                        }
//                        .border(
//                            width = 2.dp,
//                            color = mainUiState.dominantColor,
//                            shape = MaterialTheme.shapes.extraLarge
//                        )
//                        .fillMaxWidth()
//                        .clip(MaterialTheme.shapes.extraLarge)
//                        .aspectRatio(1f / 1f),
//                )
//            }
//
//            DetailBox(
//                modifier = modifier,
//                wallpaper = wallpaper,
//                color = autoThemedPaletteColor(
//                    mainUiState = mainUiState,
//                    color = BaseColor.ThemeVibrant
//                ),
//            )
//
//            Box(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                contentAlignment = Alignment.Center,
//            ) {
//                val entity = wallpaper.toWallpaperEntity(isFavorite = true)
//
//                if (mainUiState.favoriteWallpapers.contains(entity)) {
//                    FavoriteHeartButton(
//                        color = autoThemedPaletteColor(
//                            mainUiState = mainUiState,
//                            color = BaseColor.ThemeMuted
//                        ),
//                        isFavorite = true
//                    ) {
//                        onEvent.apply {
//                            this(MainUiEvents.RemoveFromFavorite(entity))
//                            this(MainUiEvents.ShowSnackBar("Removed from favorites"))
//                        }
//                    }
//                } else {
//                    FavoriteHeartButton(
//                        color = autoThemedPaletteColor(
//                            mainUiState = mainUiState,
//                            color = BaseColor.ThemeVibrant
//                        ),
//                        isFavorite = false) {
//                        onEvent.apply {
//                            this(MainUiEvents.AddToFavorite(wallpaper))
//                            this(MainUiEvents.ShowSnackBar("Added to favorites"))
//                        }
//                    }
//                }
//            }
//            ButtonBox(
//                wallpaper = wallpaper,
//               // onEvent = onEvent,
//                color = autoThemedPaletteColor(
//                    mainUiState = mainUiState,
//                    color = BaseColor.ThemeVibrant,
//                ),
//              //  mainUiState = mainUiState
////                contentColor = autoThemedPaletteColor(
////                    mainUiState = mainUiState,
////                    color = BaseColor.Vibrant,
////                )
//            )
//        }
//    }
//}
//
//

















