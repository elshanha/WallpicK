package com.elshan.wallpick.utils.custom

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Splitscreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.main.domain.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    modifier: Modifier = Modifier,
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

            Text(
                text = "Set Wallpaper as", style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 16.dp)
            )

        TextButton(onClick = {
            scope.launch {
                setWallpaper(context, wallpaper.imageUrl, WallpaperManager.FLAG_SYSTEM)
            }
        }) {
            Icon(imageVector = Icons.Rounded.Home, contentDescription = "Home")
            Text(text = "Home Screen", modifier = Modifier.padding(start = 8.dp))
        }
        TextButton(onClick = {
            scope.launch {
                setWallpaper(context, wallpaper.imageUrl, WallpaperManager.FLAG_LOCK)
            }
        }) {
            Icon(imageVector = Icons.Rounded.Lock, contentDescription = "Lock")
            Text(text = "Lock Screen", modifier = Modifier.padding(start = 8.dp))
        }
        TextButton(
            modifier = modifier.padding(bottom = 16.dp),
            onClick = {
            scope.launch {
                setWallpaper(
                    context,
                    wallpaper.imageUrl,
                    WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                )
            }
        }) {
            Icon(imageVector = Icons.Rounded.Splitscreen, contentDescription = "Both")
            Text(text = "Both", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

suspend fun setWallpaper(context: Context, imageUrl: String, flag: Int) {
    withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(bitmap, null, true, flag)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Wallpaper set successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
            }
        }
    }
}