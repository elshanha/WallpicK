package com.elshan.wallpick.main.data.remote

import com.elshan.wallpick.main.domain.model.Wallpaper

interface ApiFirebase {
    suspend fun fetchFromFirebase(
        query: String
    ): List<Wallpaper>
}