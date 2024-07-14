package com.elshan.wallpick.utils.animation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.elshan.wallpick.WallpicKApp
import com.elshan.wallpick.presentation.components.toSoftwareBitmap
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//@Composable
//fun ExtractColorsAndUploadToFirestore(imageUrl: String, documentId: String, category: String) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val firestore = FirebaseFirestore.getInstance()
//    val imageLoader = context.imageLoader
//    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context)
//            .data(imageUrl)
//            .crossfade(true)
//            .diskCacheKey(imageUrl)
//            .memoryCacheKey(imageUrl)
//            .size(Size.ORIGINAL)
//            .build(),
//        imageLoader = imageLoader
//    )
//
//    when (val imageState = imagePainter.state) {
//        is AsyncImagePainter.State.Success -> {
//            val drawable = imageState.result.drawable
//            val bitmap = (drawable as? BitmapDrawable)?.bitmap
//
//            bitmap?.let {
//                LaunchedEffect(it) {
//                    val softwareBitmap = it.toSoftwareBitmap()
//                    val palette = Palette.from(softwareBitmap).generate()
//                    val colors = extractColors(it)
//                    uploadColorsToFirestore(colors, documentId, category, firestore)
//                }
//            }
//        }
//
//        AsyncImagePainter.State.Empty -> TODO()
//        is AsyncImagePainter.State.Error -> TODO()
//        is AsyncImagePainter.State.Loading -> TODO()
//    }
//}
//
//
//fun Bitmap.toSoftwareBitmap(): Bitmap {
//    return if (config == Bitmap.Config.HARDWARE) {
//        copy(Bitmap.Config.ARGB_8888, false)
//    } else {
//        this
//    }
//}
//
//fun extractColors(bitmap: Bitmap): List<String> {
//    val softwareBitmap = bitmap.toSoftwareBitmap()
//    val palette = Palette.from(softwareBitmap).generate()
//    val colors = mutableListOf<String>()
//
//    palette.dominantSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.vibrantSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.mutedSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.darkVibrantSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.lightVibrantSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.darkMutedSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//    palette.lightMutedSwatch?.let { swatch ->
//        colors.add("#${Integer.toHexString(swatch.rgb)}")
//    }
//
//    return colors
//}
//
//suspend fun uploadColorsToFirestore(colors: List<String>, documentId: String, category: String, firestore: FirebaseFirestore) {
//    val wallpaperRef = firestore.collection("categories")
//        .document(category)
//        .collection("wallpapers")
//        .document(documentId)
//
//    wallpaperRef.update("colors", colors).await()
//}


 fun extractColorsAndUploadToFirestore(imageUrl: String, documentId: String, category: String, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val imageLoader = context.imageLoader
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .target { drawable ->
            val bitmap = (drawable as BitmapDrawable).bitmap
//            val softwareBitmap = bitmap.toSoftwareBitmap()
//            val palette = Palette.from(softwareBitmap).generate()
//            val allSwatches = palette.swatches.sortedByDescending { it.population }
//            val colors = allSwatches.take(3).map { swatch ->
//                String.format("#%06X", 0xFFFFFF and swatch.rgb)
//            }

            val dominantColors = extractDominantColors(bitmap)
            val colors = dominantColors.map { String.format("#%06X", 0xFFFFFF and it) }

            val wallpaperRef = db.collection("categories")
                .document(category)
                .collection("wallpapers")
                .document(documentId)

            wallpaperRef.update("colors", colors)
                .addOnSuccessListener {
                    Log.d("ColorExtraction", "Colors updated successfully for wallpaper $documentId")
                }
                .addOnFailureListener { e ->
                    Log.e("ColorExtraction", "Failed to update colors for wallpaper $documentId", e)
                }
        }
        .build()

    imageLoader.enqueue(request)
}

fun extractDominantColors(bitmap: Bitmap, numColors: Int = 4): List<Int> {
    val softwareBitmap = bitmap.toSoftwareBitmap()
    val palette = Palette.from(softwareBitmap).maximumColorCount(numColors).generate()
    return palette.swatches
        .sortedByDescending { it.population }
        .take(numColors)
        .map { it.rgb }
}