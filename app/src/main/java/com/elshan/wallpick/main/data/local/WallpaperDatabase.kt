package com.elshan.wallpick.main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WallpaperEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract val wallpaperDao: WallpaperDao
}