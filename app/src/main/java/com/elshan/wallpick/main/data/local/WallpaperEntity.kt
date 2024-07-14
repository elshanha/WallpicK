package com.elshan.wallpick.main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "wallpapers")
data class WallpaperEntity(
    val category: String,
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val imageUrl: String,
    val name: String,
    val resolution: String,
    val tags : List<String> = emptyList(),
    val localPath: String? = null,
    var isFavorite: Boolean = false
)