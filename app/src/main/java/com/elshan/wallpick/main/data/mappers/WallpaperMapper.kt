package com.elshan.wallpick.main.data.mappers

import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

fun WallpaperEntity.toWallpaper(): Wallpaper {
    return Wallpaper(
        category = this.category,
        id = this.id,
        imageUrl = this.imageUrl,
        name = this.name,
        resolution = this.resolution,
        tags = this.tags,
    )

}

fun Wallpaper.toWallpaperEntity(isFavorite: Boolean = false): WallpaperEntity {
    return WallpaperEntity(
        category = this.category,
        id = this.id,
        imageUrl = this.imageUrl,
        name = this.name,
        resolution = this.resolution,
        tags = this.tags,
        isFavorite = isFavorite,
    )
}

fun DocumentSnapshot.toWallpaper(): Wallpaper? {
    return try {
        val category = getString("category") ?: return null
        val colors = get("colors") as? List<String> ?: return null
        val downloads = getLong("downloads") ?: 0
        val id = getString("id") ?: return null
        val imageUrl = getString("imageUrl") ?: return null
        val name = getString("name") ?: return null
        val resolution = getString("resolution") ?: return null
        val tags = get("tags") as? List<String> ?: return null
        val views = getLong("views") ?: 0
        Wallpaper(
            category = category,
            colors = colors,
            downloads = downloads,
            id = id,
            imageUrl = imageUrl,
            name = name,
            resolution = resolution,
            tags = tags,
            views = views,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
