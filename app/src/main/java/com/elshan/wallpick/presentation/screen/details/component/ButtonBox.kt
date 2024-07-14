package com.elshan.wallpick.presentation.screen.details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.custom.CustomModalBottomSheet


@Composable
fun ButtonBox(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    wallpaper: Wallpaper,
    onEvent: (MainUiEvents) -> Unit,
    color: Color = ButtonDefaults.filledTonalButtonColors().containerColor,
    contentColor: Color = ButtonDefaults.filledTonalButtonColors().contentColor
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showBottomSheet) {
        CustomModalBottomSheet(
            wallpaper = wallpaper,
            onDismissRequest = { showBottomSheet = false }
        )
    }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        InfoDialog(
            title = "Hey!",
            message = "Wallpaper is already downloaded.\nDo you want to download it again?",
            onDismissRequest = { showDialog = false },
            onConfirm = {
                showDialog = false
                onEvent(MainUiEvents.DownloadWallpaper(wallpaper))
                onEvent(MainUiEvents.TrackEvent(wallpaper.category, wallpaper.id, "downloads"))
            }
        )
    }
    val isDownloaded = mainUiState.downloadedWallpapers.contains(wallpaper.id)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        FilledTonalButton(
            colors = ButtonDefaults.filledTonalButtonColors().copy(
                containerColor = color,
                contentColor = contentColor
            ),
            onClick = {
                showBottomSheet = true
            }) {
            Text(text = "Apply")
        }
        FilledTonalButton(
            colors = ButtonDefaults.filledTonalButtonColors().copy(
                containerColor = color,
                contentColor = contentColor
            ),
            onClick = {
                if (isDownloaded) {
                    showDialog = true
                } else {
                    onEvent(MainUiEvents.SetWallpaperDownloaded(wallpaper.id, true))
                    onEvent(MainUiEvents.DownloadWallpaper(wallpaper))
                    onEvent(MainUiEvents.TrackEvent(wallpaper.category, wallpaper.id, "downloads"))
                }
            }) {
            Text(text = if (isDownloaded) "Saved" else "Save")
        }
        FilledTonalButton(
            colors = ButtonDefaults.filledTonalButtonColors().copy(
                containerColor = color,
                contentColor = contentColor
            ),
            onClick = {
                onEvent(MainUiEvents.ShareWallpaper(wallpaper, context))
            }) {
            Text(text = "Share")
        }
    }

}
