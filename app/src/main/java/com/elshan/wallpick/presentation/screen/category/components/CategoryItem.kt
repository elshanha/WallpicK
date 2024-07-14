package com.elshan.wallpick.presentation.screen.category.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.elshan.wallpick.R
import com.elshan.wallpick.main.domain.model.Category
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.viewmodel.MainViewModel


@Composable
fun CategoryItem(
    category: Category,
    navController: NavController,
    isGridView: Boolean = false,
    onEvent: (MainUiEvents) -> Unit
) {

    Box(
        modifier = Modifier
            .aspectRatio(
                if (isGridView) 3f / 2f else 2.35f / 1f
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        ImageWithPalette(
            imageUrl = category.bgUrl,
            loadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.photo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .aspectRatio(if (isGridView) 3f / 2f else 2.35f / 1f)
                )
            }
        ) { imagePainter ->
            Image(
                painter = imagePainter,
                contentScale = ContentScale.Crop,
                alpha = 0.7f,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent.apply {
                            this(MainUiEvents.GetWallpapersByCategory(category.name))
                        }
                        navController.navigate(
                            Screen.WallpaperByCategory(
                                category = category.name
                            )
                        )
                    }
            )
        }
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = category.name,
                    style = if (isGridView) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = if (category.wallpaperCount == 0) "No wallpapers"
                    else "${category.wallpaperCount} wallpapers",
                    style = if (isGridView) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyLarge,
                )
            }
        }

    }
}