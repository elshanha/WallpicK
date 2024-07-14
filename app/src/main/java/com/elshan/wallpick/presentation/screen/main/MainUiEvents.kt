package com.elshan.wallpick.presentation.screen.main

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.utils.custom.FilterType
import com.google.firebase.auth.FirebaseUser

sealed class MainUiEvents() {
    data object GetFromFirebase : MainUiEvents()
    data class GetWallpapersByCategory(val category: String) : MainUiEvents()
    data class AddToFavorite(val wallpaper: Wallpaper) : MainUiEvents()
    data class RemoveFromFavorite(val wallpaperEntity: WallpaperEntity) : MainUiEvents()
    data object GetFavorites : MainUiEvents()
    data object DeleteAllFavorites : MainUiEvents()
    data class GetMostDownloadedWallpapers(val minDownloads: Long) : MainUiEvents()
    data class FilterWallpapers(val filterType: FilterType) : MainUiEvents()
    data class GetPopularWallpapers(val minViews: Long) : MainUiEvents()

    data class TrackEvent(val category: String, val wallpaperId: String, val interactionType: String) : MainUiEvents()

    data class ShowToast(val message: String) : MainUiEvents()
    data class ShowSnackBar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration? = null,
        val customDurationMillis: Long? = null,
        val isBottomAppBarVisible: Boolean? = null
    ) : MainUiEvents()

    data class SearchWallpapers(val query: String) : MainUiEvents()
    data class ColorSelected(val color: Color) : MainUiEvents()
    data class SearchFavorites(val query: String) : MainUiEvents()
    data class DownloadWallpaper(val wallpaper: Wallpaper, ) : MainUiEvents()
    data class ShareWallpaper(val wallpaper: Wallpaper, val context: Context) : MainUiEvents()
    data class ShowNotification(val value: String) : MainUiEvents()

    data class SetPaletteColors(val palette: Palette) : MainUiEvents()

    data class StoreUserInfo(val user : FirebaseUser) : MainUiEvents()
    data class GetUserInfo(val uid : String) : MainUiEvents()

    // Login
    data object SignOut : MainUiEvents()
    data object SignIn : MainUiEvents()
    data object ResetState : MainUiEvents()

    // Datastore Preferences
    data object LoadLayoutPreference : MainUiEvents()
    data class SaveLayoutPreference(val isGridLayout: Boolean) : MainUiEvents()
    data class SetWallpaperDownloaded(val wallpaperId: String, val downloaded: Boolean) : MainUiEvents()
    data class CheckWallpaperDownloaded(val wallpaperId: String) : MainUiEvents()
    data object LoadNotificationPreference : MainUiEvents()
    data class SaveNotificationPreference(val enabled: Boolean) : MainUiEvents()
    data class ToggleNotification(val enabled: Boolean) : MainUiEvents()
    data object CompleteOnboarding : MainUiEvents()


}
