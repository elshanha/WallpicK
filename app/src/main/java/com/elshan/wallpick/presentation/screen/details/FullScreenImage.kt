package com.elshan.wallpick.presentation.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.R
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.utils.custom.CustomModalBottomSheet
import com.elshan.wallpick.presentation.components.ImageWithPalette
import com.elshan.wallpick.utils.bottomWindowInsetsPadding

data class FullScreenImageState(
    var showInfo: Boolean = false,
    val showBottomSheet: Boolean = false
)

@Composable
fun FullScreenImage(
    modifier: Modifier = Modifier,
    wallpaper: Wallpaper,
    onEvent: (MainUiEvents) -> Unit
) {

  //  SetupSystemUi(useDarkIcons = false, systemBarsColor = Color.Transparent)


    ImageWithPalette(imageUrl = wallpaper.imageUrl) { imagePainter ->
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = wallpaper.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun CustomButtonsSection(
    modifier: Modifier = Modifier,
    wallpaper: Wallpaper,
    onEvent: (MainUiEvents) -> Unit
) {
    var state by remember {
        mutableStateOf(FullScreenImageState())
    }


//    if (state.showInfo) {
//        InfoDialog(
//            modifier = modifier,
//            title = "Info",
//            message = " Category: ${wallpaper.category.replaceFirstChar { it.uppercase() }}\n Name: ${wallpaper.name}\n Resolution: ${wallpaper.resolution}",
//            onConfirm = { state = state.copy(showInfo = false) }
//        ) {
//            state = state.copy(showInfo = false)
//        }
//    }

    if (state.showBottomSheet) {
        CustomModalBottomSheet(
            wallpaper = wallpaper,
            onDismissRequest = { state = state.copy(showBottomSheet = false) }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .padding(bottom = bottomWindowInsetsPadding() + 16.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CustomIconButton(
                painter = painterResource(id = R.drawable.ic_information_square),
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                state = state.copy(showInfo = true)
            }
            CustomIconButton(
                painter = painterResource(id = R.drawable.ic_download_square),
                contentDescription = "Download",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                onEvent(MainUiEvents.DownloadWallpaper(wallpaper))
            }

            CustomIconButton(
                painter = painterResource(id = R.drawable.ic_paint_brush),
                contentDescription = "Set as wallpaper",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                state = state.copy(showBottomSheet = true)
            }
        }
    }

}

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    tint: Color = Color.White,
    onIconClick: () -> Unit,
) {
    IconButton(
        onClick = {
            onIconClick()
        }) {
        Icon(
            modifier = modifier
                .size(48.dp),
            painter = painter,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}










