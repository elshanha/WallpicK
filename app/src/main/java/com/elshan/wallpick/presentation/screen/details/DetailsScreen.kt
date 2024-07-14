package com.elshan.wallpick.presentation.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.main.data.mappers.toWallpaperEntity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.presentation.screen.details.component.ButtonBox
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.animation.FavoriteHeartButton

@Composable
fun DetailsScreen(
    wallpaper: Wallpaper,
    onEvent: (MainUiEvents) -> Unit,
    mainUiState: MainUiState,
    navController: NavController
) {
    LaunchedEffect(wallpaper.id) {
        onEvent(MainUiEvents.CheckWallpaperDownloaded(wallpaper.id))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = wallpaper.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        LocalContentColor.current
                    ),
                )
                Text(
                    text = wallpaper.category.first().uppercase() + wallpaper.category.substring(1),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }

            val entity = wallpaper.toWallpaperEntity(isFavorite = true)

            if (mainUiState.favoriteWallpapers.contains(entity)) {
                FavoriteHeartButton(
                    isFavorite = true
                ) {
                    onEvent.apply {
                        this(MainUiEvents.RemoveFromFavorite(entity))
                        this(MainUiEvents.ShowSnackBar("Removed from favorites"))
                    }
                }
            } else {
                FavoriteHeartButton(
                    isFavorite = false
                ) {

                    onEvent.apply {
                        this(MainUiEvents.AddToFavorite(wallpaper))
                        this(MainUiEvents.ShowSnackBar("Added to favorites"))
                    }
                }
            }
        }

        ImageWithPalette(
            imageUrl = wallpaper.imageUrl,
            onColorExtracted = {
                onEvent(MainUiEvents.SetPaletteColors(it))
            }
        ) { imagePainter ->
            Image(
                painter = imagePainter,
                contentDescription = "Wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        navController.navigate(
                            Screen.FullScreenImage(
                                category = wallpaper.category,
                                imageUrl = wallpaper.imageUrl,
                                name = wallpaper.name,
                                resolution = wallpaper.resolution,
                                id = wallpaper.id
                            )
                        )
                        onEvent(
                            MainUiEvents.TrackEvent(
                                wallpaper.category,
                                wallpaper.id,
                                "views"
                            )
                        )
                    }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ButtonBox(
            mainUiState = mainUiState,
            wallpaper = wallpaper,
            onEvent = onEvent,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDetailsScreen() {
    val wallpaper = Wallpaper(
        category = "Movies",
        id = "1",
        imageUrl = "https://example.com/image.jpg",
        name = "Batman",
        resolution = "1080x1920",
        tags = listOf("Action", "Hero"),
    )

    DetailsScreen(
        wallpaper = wallpaper,
        onEvent = {},
        mainUiState = MainUiState(),
        navController = NavController(LocalContext.current)
    )
}
