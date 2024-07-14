package com.elshan.wallpick.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.elshan.wallpick.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ImageWithPalette(
    imageUrl: String,
    onColorExtracted: (Palette) -> Unit = {},
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    content: @Composable (Painter) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val imagePainter = rememberAsyncImagePainter(
        imageLoader = imageLoader,
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .diskCacheKey(imageUrl)
            .memoryCacheKey(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    val systemUiController = rememberSystemUiController()

    when (val imageState = imagePainter.state) {
        is AsyncImagePainter.State.Loading -> {
            loadingContent()
        }

        is AsyncImagePainter.State.Error -> {
            Image(
                painter = painterResource(id = R.drawable.image_not_found),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .aspectRatio(3f / 4f)
            )
        }

        is AsyncImagePainter.State.Empty -> {

        }

        is AsyncImagePainter.State.Success -> {
            val drawable = imageState.result.drawable
            val bitmap = (drawable as? BitmapDrawable)?.bitmap

            bitmap?.let {
                LaunchedEffect(it) {
                    val softwareBitmap = it.toSoftwareBitmap()
                    val palette = Palette.from(softwareBitmap).generate()
                    onColorExtracted(palette)
                }
            }
            content(imagePainter)
        }
    }


}


fun Bitmap.toSoftwareBitmap(): Bitmap {
    return if (config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, false)
    } else {
        this
    }
}