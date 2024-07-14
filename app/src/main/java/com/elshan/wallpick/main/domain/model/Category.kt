package com.elshan.wallpick.main.domain.model

data class Category(
    val bgUrl : String,
    val name: String,
    var wallpaperCount: Int = 0
)
