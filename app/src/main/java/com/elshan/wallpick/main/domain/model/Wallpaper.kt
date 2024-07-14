package com.elshan.wallpick.main.domain.model


data class Wallpaper(
    val category: String,
    val colors: List<String> = emptyList(),
    val id: String,
    val imageUrl: String,
    val name: String,
    val resolution: String,
    val tags: List<String> = emptyList(),
    val views: Long = 0,
    val downloads: Long = 0
)


