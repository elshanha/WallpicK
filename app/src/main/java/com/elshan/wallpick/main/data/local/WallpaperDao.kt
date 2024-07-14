package com.elshan.wallpick.main.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaperList(wallpaper: List<WallpaperEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: WallpaperEntity)

    @Query("SELECT * FROM wallpapers WHERE imageUrl = :imageUrl LIMIT 1")
    suspend fun getWallpaperByUrl(imageUrl: String): WallpaperEntity?

    @Query("SELECT * FROM wallpapers")
    suspend fun getAll(): List<WallpaperEntity>

    @Query("DELETE FROM wallpapers")
    suspend fun deleteAll()

    @Query("SELECT * FROM wallpapers WHERE isFavorite = 1")
    suspend fun getFavoriteWallpapers(): List<WallpaperEntity>

    @Query("DELETE FROM wallpapers WHERE isFavorite = 1")
    suspend fun deleteAllFavorites()

    @Delete
    suspend fun deleteWallpaper(wallpaper: WallpaperEntity)

    @Update
    suspend fun updateWallpaper(wallpaper: WallpaperEntity)

}