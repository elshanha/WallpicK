package com.elshan.wallpick.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {


    @Serializable
    data object Main : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object AllWallpapers : Screen()

    @Serializable
    data class FullScreenImage(
        val category: String,
        val imageUrl: String,
        val name: String,
        val resolution: String,
        val id: String,
    ) : Screen()

    @Serializable
    data class Profile(
        val createdAt: Long = 0,
        val email: String = "",
        val name: String = "",
        val profilePicture: String = "",
        val uid: String = "",
        val title: String,
        val description: String,
    ) : Screen()

    @Serializable
    data class WallpaperDetails(
        val category: String,
        val id: String,
        val imageUrl: String,
        val name: String,
        val resolution: String,
        val tags: List<String>,
    ) : Screen()

    @Serializable
    data class WallpaperByCategory(
        val category: String,
    ) : Screen()

    @Serializable
    data object MostDownloadedWallpapers : Screen()

    @Serializable
    data object PopularWallpapers : Screen()

    @Serializable
    data object Search : Screen()


}