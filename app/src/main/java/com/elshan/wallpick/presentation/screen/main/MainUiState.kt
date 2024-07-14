package com.elshan.wallpick.presentation.screen.main

import android.content.IntentSender
import androidx.compose.ui.graphics.Color
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.domain.model.Category
import com.elshan.wallpick.main.domain.model.User
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.utils.custom.FilterType
import com.google.firebase.auth.FirebaseUser

data class MainUiState(
    // Login
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val intentSender: IntentSender? = null,
    val user: User? = null,
    val fireBaseUser: FirebaseUser? = null,

    // Wallpaper
    val allWallpapers: List<Wallpaper> = emptyList(),
    val categoryWallpapers: List<Wallpaper> = emptyList(),
    val wallpapersByCategory: List<Wallpaper> = emptyList(),
    val categories: List<Category> = emptyList(),
    val favoriteWallpapers: List<WallpaperEntity> = emptyList(),
    val popularWallpapers: List<Wallpaper> = emptyList(),
    val mostDownloadedWallpapers: List<Wallpaper> = emptyList(),
    val isDownloaded: Boolean = false,
    val filteredFavorites: List<WallpaperEntity> = emptyList(),
    val filteredWallpapers: List<Wallpaper> = emptyList(),
    val selectedFilter: FilterType = FilterType.LATEST,
    var favoritesQuery: String = "",
    val errorMessage: String = "",
    var searchWallpapers: List<Wallpaper> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "",
    val tags: String = "",
    val downloadedWallpapers: Set<String> = emptySet(),
    val selectedColor: Color? = null,
    val selectedTag: String? = null,
    val tagsList: List<String> = emptyList(),


    // Settings
    val notificationsEnabled: Boolean = true,
    val biometricsEnabled: Boolean = false,


    // Layout
    var isGridLayout: Boolean = false,
    var isGridList: Boolean = false,
    val onboardingCompleted: Boolean = false,
    val isReady : Boolean = false,

    // Color
    val dominantColor: Color = Color.Transparent,
    val vibrantColor: Color = Color.Transparent,
    val mutedColor: Color = Color.Transparent,
    val darkMutedColor: Color = Color.Transparent,
    val lightMutedColor: Color = Color.Transparent,
    val lightVibrantColor: Color = Color.Transparent,
    val darkVibrantColor: Color = Color.Transparent,
    val bodyTextColor: Color = Color.Transparent,
    val titleTextColor: Color = Color.Transparent,

    )