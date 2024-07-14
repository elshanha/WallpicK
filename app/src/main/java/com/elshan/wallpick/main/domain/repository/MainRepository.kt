package com.elshan.wallpick.main.domain.repository

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.domain.model.Category
import com.elshan.wallpick.main.domain.model.User
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    // Local Repository
    suspend fun insertWallpaper(wallpaperEntity: WallpaperEntity)
    suspend fun deleteWallpaper(wallpaperEntity: WallpaperEntity)
    suspend fun updateWallpaper(wallpaperEntity: WallpaperEntity)
    suspend fun getAllWallpapers(): Flow<List<WallpaperEntity>>
    suspend fun getWallpaperByUrl(imageUrl: String): Wallpaper
    suspend fun addOrRemoveFavorites(imageUrl: String, isFavorite: Boolean)
    suspend fun getFavorites(): Flow<List<WallpaperEntity>>
    suspend fun deleteAllFavorites(): Flow<List<WallpaperEntity>>
    suspend fun deleteAllWallpapers(): Flow<List<WallpaperEntity>>

    // Remote Repository
    suspend fun fetchWallpapers(
        fetchFromRemote: Boolean,
    ): Flow<Resource<List<Wallpaper>>>

    suspend fun fetchCategories(): Flow<Resource<List<Category>>>

    suspend fun fetchWallpapersByCategory(
        categoryName: String = "abstract",
    ): Flow<List<Wallpaper>>

    suspend fun extractColorsForNewWallpapers(context: Context)

    suspend fun fetchAndCacheWallpapers(): List<Wallpaper>
    suspend fun searchWallpapers(query: String): Flow<List<Wallpaper>>
    suspend fun searchWallpapersByColor(targetColor: Color): Flow<List<Wallpaper>>
    suspend fun downloadWallpaper(context: Context, imageUrl: String, imageName: String): Boolean
    suspend fun trackInteraction(category: String, wallpaperId: String, interactionType: String)
    suspend fun getPopularWallpapers(minViews: Long): Flow<List<Wallpaper>>
    suspend fun getMostDownloadedWallpapers(minDownloads: Long): Flow<List<Wallpaper>>

    // User Repository
    suspend fun storeUserInfo(user: FirebaseUser): Flow<Resource<Unit>>
    suspend fun getUserInfo(uid: String): Flow<Resource<User?>>

}